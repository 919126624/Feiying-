package com.wuyizhiye.basedata.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.code.enums.PriorityEnum;
import com.wuyizhiye.basedata.code.enums.PropertyEnum;
import com.wuyizhiye.basedata.code.model.BillType;
import com.wuyizhiye.basedata.code.model.Record;
import com.wuyizhiye.basedata.code.model.RuleItems;
import com.wuyizhiye.basedata.code.model.Rules;
import com.wuyizhiye.basedata.org.model.Org;

/**
 * @ClassName GenerateKey
 * @Description 生成编号类
 * @author li.biao
 * @date 2015-4-2
 */
public class GenerateKey {
	private static Logger logger=Logger.getLogger(GenerateKey.class);
	private static final String ORGTYPE = "GROUP";  
	/**
	 * 根据组织、业务类型生成编号
	 * @param orgId  组织ID
	 * @param type   业务类型
	 * @return  编号
	 * @throws Exception
	 */
	public synchronized static String getKeyCode(String orgId, String type) throws Exception {
		String keyCode = null;
		//如果当前传入的组织为空,则以集团定义的规则
		if(StringUtils.isEmpty(orgId)){
			return getGroupRules(type);
		}
		Rules rules = getRulesEntity(orgId, type);
		//如果当前组织规则未定义,则直接使用集团定义的规则
		if (rules != null && rules.getId() != null) {
			// 组织优先
			if (PriorityEnum.ORGPRIORITY.equals(rules.getPriority())) {
				keyCode = getOrgRules(rules);
			}
			// 集团优先
			if (PriorityEnum.GROUPPRIORITY.equals(rules.getPriority())) {
				keyCode = getGroupRules(type);
			}
		} else {
			keyCode = getGroupRules(type);
		}
		return keyCode;
	} 
	
	
	/**
	 * 根据编号禁用编号生成记录
	 * @param key  单据编号
	 * @param orgid  单据编号所属组织  如果为集团则为空(null)
	 * @param billTypeId 单据编号类型
	 */
	public static void updateKeyCode(String key,String orgid ,String billType) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("status", "N");
		param.put("code", key);
		param.put("billType", billType);
		param.put("orgid", orgid);
		getQueryExecutor().executeUpdate("com.wuyizhiye.basedata.code.RecordDao.updateStatus", param);
	}
	 
	/**
	 * 获取集团自定义的规则
	 * @return
	 * @throws Exception 
	 */
	private static String getGroupRules(String type) throws Exception{
		String keyCode = null;
		Org org = getOrgByType();
		keyCode = getOrgRules(getRulesEntity(org.getId(), type));
		return  keyCode;
	}

	
	/**
	 * 根据规则获取编号记录
	 * @param ruleId   编号主键
	 * @param orderBy  用于排序 DESC  ASC
 	 * @param status   编号记录状态  N Y
	 * @return
	 */
	private static Record getRecordByRule(String ruleId, String orderBy,String status,String reBuild) {
		Map<String, Object> recordParam = new HashMap<String, Object>();
		//是否需要按年,月,日重新生成编码规则
		if(StringUtils.isNotNull(reBuild)){
			SimpleDateFormat   sf = new SimpleDateFormat(reBuild);
			recordParam.put("reBuild",sf.format(new Date()));
		}
		recordParam.put("ruleId", ruleId);
		recordParam.put("orderBy", orderBy);
		recordParam.put("status", status);
		List<Record> recordItems = getQueryExecutor().execQuery("com.wuyizhiye.basedata.code.RecordDao.select", recordParam,Record.class);
		if (recordItems != null && recordItems.size() > 0)
			return recordItems.get(0);
		return new Record();
	}

	/**
	 * 记录生成的编号
	 * 
	 * @param record
	 */
	private static void inserRecordKey(Record record) {
		getQueryExecutor().executeInsert("com.wuyizhiye.basedata.code.RecordDao.insert", record);
	}

	/**
	 * 修改编号的记录信息
	 * 
	 * @param record
	 */
	private static void updateRecordKey(Record record) {
		getQueryExecutor().executeInsert("com.wuyizhiye.basedata.code.RecordDao.update", record);
	}

	/**
	 * 获取自定义规则
	 * 
	 * @param org
	 * @param type
	 * @return
	 * @throws Exception 
	 */
	private static Rules getRulesEntity(String org, String type) throws Exception {
		Map<String, Object> ruleParam = new HashMap<String, Object>();
		ruleParam.put("orgId", org);
		ruleParam.put("typeCode", type);
		Rules rules=null;
		try {
			rules = (Rules) getQueryExecutor().execOneEntity("com.wuyizhiye.basedata.code.RulesDao.selectByOrg", ruleParam,Rules.class);
		} catch (Exception e) {
			logger.error("", e);
			throw new Exception("该组织定义了多套生成编号的规则,请联系管理员。");
		}
		if(rules!=null&&rules.getId()!=null)
			return rules;
		return new Rules();
	}

	/**
	 * 获取自定义明细规则,并且生成编号
	 * 
	 * @param codeKey
	 * @param rules
	 * @return
	 * @throws Exception 
	 */
	private static String getOrgRules(Rules rules) throws Exception {
		if(!StringUtils.isNotNull(rules)){
			throw new Exception("该组织未定义生成编号的规则,请联系管理员。");
		}
		Map<String, Object> ruleItemsParam = new HashMap<String, Object>();
		ruleItemsParam.put("ruleId", rules.getId());
		List<RuleItems> ruleItems = getQueryExecutor().execQuery("com.wuyizhiye.basedata.code.RuleItemsDao.select",ruleItemsParam, RuleItems.class);

		Record record = new Record();
		String codeKey = new String(); // 存储编号

		boolean codeKeyFlag = true; // 生成编号是否产生记录

		for (RuleItems items : ruleItems) {
			// 固定值
			if (PropertyEnum.FIXEDVALUE.equals(items.getProperty())) {
				codeKey += items.getFormat();
				continue;
			}
			// 日期
			if (PropertyEnum.FORMATDATE.equals(items.getProperty())) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(items.getFormat());
				codeKey += dateFormat.format(new Date());
				continue;
			}
			// 序列号
			if (PropertyEnum.SEQUENCE.equals(items.getProperty())) {
				int seqNo = 0;
				
				Record checkRecord = getRecordByRule(rules.getId(), "DESC", "Y",rules.getReBuild());
				
				// 规则是否已经生成过编号
				if (checkRecord != null && checkRecord.getId() != null) {
					String numLength=(checkRecord.getSeqNumber()+1)+"";
					if(!(ruleItems.size()==1 && ruleItems.get(0).getNumLength()==0)){
						//判断编号是否与定义好的规则相符合
						if(items.getNumLength()<numLength.length()){
							throw new Exception("该组织定义的编号规则中的序列号已经不够用,请联系管理员。");
						}
					}
					
					// 规则配置 是否有(不允许断号)标识
					if ("Y".equals(rules.getIsInterrupt())) {
						Record disableRecord = getRecordByRule(rules.getId(),"ASC", "N",rules.getReBuild());
						if (disableRecord != null && disableRecord.getId() != null) { // 有被禁用的编号
							seqNo = disableRecord.getSeqNumber();
							codeKey += formatNumber(new Integer(seqNo),new Integer(items.getNumLength()));
							disableRecord.setStatus("Y"); 
							// 将状态修改 启用
							updateRecordKey(disableRecord);  
							codeKeyFlag = false;
							continue;
						}
					}

					seqNo = checkRecord.getSeqNumber() + 1;
					record.setSeqNumber(new Integer(seqNo));
					record.setSequences(formatNumber(new Integer(seqNo),new Integer(items.getNumLength())));
					codeKey += formatNumber(new Integer(seqNo), new Integer(items.getNumLength()));
					continue;
				}
				codeKey += formatNumber(new Integer(items.getFormat()),new Integer(items.getNumLength()));
				record.setSeqNumber(new Integer(items.getFormat()));
				record.setSequences(formatNumber(new Integer(items.getFormat()),new Integer(items.getNumLength())));
				continue;
			}
			// 组织编码
			if (PropertyEnum.ORGCODE.equals(items.getProperty())) {
				//组织编码长度过长
				if(items.getNumLength()>rules.getCodeOrg().getNumber().length()){
					throw new Exception("定义编码规则中组织编码的长度太长,请联系管理员。");
				}
				if ("LEFT".equals(items.getFormat())) {
					codeKey += rules.getCodeOrg().getNumber().substring(0, items.getNumLength()==0?rules.getCodeOrg().getNumber().length():items.getNumLength());
				} else if ("RIGHT".equals(items.getFormat())) {
					codeKey += rules.getCodeOrg().getNumber().substring(items.getNumLength()==0?0:rules.getCodeOrg().getNumber().length()-items.getNumLength());
				}
				continue;
			}
			// 组织简称
			if (PropertyEnum.ORGABBREVIATION.equals(items.getProperty())) {
				//组织简称长度过长
				if(items.getNumLength()>rules.getCodeOrg().getSimplePinyin().length()){
					throw new Exception("定义编码规则中组织简称的长度太长,请联系管理员。");
				}
				if ("LEFT".equals(items.getFormat())) {
					codeKey += rules.getCodeOrg().getSimplePinyin().substring(0, items.getNumLength()==0?rules.getCodeOrg().getSimplePinyin().length():items.getNumLength());
				} else if ("RIGHT".equals(items.getFormat())) {
					codeKey += rules.getCodeOrg().getSimplePinyin().substring(items.getNumLength()==0?0:rules.getCodeOrg().getSimplePinyin().length()-items.getNumLength());
				}
				continue;
			}
		}
		// 重新生成编号记录
		if (codeKeyFlag) {
			BillType billType = new BillType();
			billType.setId(rules.getType().getId());
			record.setRules(rules);
			record.setType(billType);
			record.setStatus("Y");
			record.setId(StringUtils.getUUID());
			record.setCreateTime(new Date());
			record.setCodeOrg(rules.getCodeOrg());
			record.setCreator(SystemUtil.getCurrentUser());
			record.setCode(codeKey);
			
			if(StringUtils.isNotNull(rules.getReBuild())){
				SimpleDateFormat  sd = new SimpleDateFormat(rules.getReBuild());
				record.setReBuild(sd.format(new Date()));
			}
			inserRecordKey(record);
		}
		return codeKey;
	}

	/**
	 * 获取组织
	 * @return
	 * @throws Exception 
	 */
	private static Org getOrgByType() throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orgType", ORGTYPE);
		List<Org> orgs = getQueryExecutor().execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.getOrgByType", param,Org.class);
		if (orgs != null && orgs.size() > 0)
			return orgs.get(0);
		throw new Exception("系统中未建立组织结构树，请联系管理员。");
	}

	/**
	 * 格式化数字,根据规则配置的长度,往左边补零
	 * 
	 * @param num
	 * @param length
	 * @return
	 */
	private static String formatNumber(Integer num, Integer length) {
		if(length==0){
			return num.toString();
		}
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumIntegerDigits(length);
		nf.setMinimumIntegerDigits(length);
		return nf.format(num).toString();
	}

	private static QueryExecutor getQueryExecutor() {
		return ApplicationContextAware.getApplicationContext().getBean("queryExecutor", QueryExecutor.class);
	}
}
