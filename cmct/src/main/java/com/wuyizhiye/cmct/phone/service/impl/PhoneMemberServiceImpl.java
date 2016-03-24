package com.wuyizhiye.cmct.phone.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.dao.PhoneMemberDao;
import com.wuyizhiye.cmct.phone.enums.PhoneEnableEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneMemberEnum;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.model.PhoneRight;
import com.wuyizhiye.cmct.phone.service.PhoneMemberService;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.cmct.phone.util.PhoneRightUtil;
import com.wuyizhiye.cmct.phone.util.ProjectMApiRemoteServer;


/**
 * @ClassName PhoneMemberServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneMemberService")
@Transactional
public class PhoneMemberServiceImpl extends DataEntityService<PhoneMember> implements PhoneMemberService {
	private static Log log = LogFactory.getLog(PhoneMemberServiceImpl.class);
	@Autowired
	private PhoneMemberDao phoneMemberDao;
	
	@Override
	protected BaseDao getDao() {
		return phoneMemberDao;
	}

	@Override
	public void updatePhoneRight(PhoneMember pm, PhoneRight pr,String type) throws Exception {
		
		Map<String,Object> map = null;
		if("bind".equals(type)){
			map = PhoneRightUtil.bindRight(pr);
		}else if("unbind".equals(type)){
			map = PhoneRightUtil.unbindRight(pr);
		}
		String state = map.get("STATE").toString();
		if("SUCCESS".equals(state)){
			if("bind".equals(type)){
				pm.setPhoneRight(pr);
			}else if("unbind".equals(type)){
				pm.setPhoneRight(null);
			}
			super.updateEntity(pm);
		}else{
			String msg = map.get("MSG").toString();
			String errNumber = map.get("errNumber").toString();
			throw new Exception(msg+" "+errNumber);
		}
	}

	@Override
	public Map<String, Object> batchMatchPhone(PhoneMember pm) {
		PhoneMember oldMember=pm;//从表单提交过来的对象
		Map<String, Object> matchResult=new HashMap<String, Object>();
		String pmJson=pm.getPmJson();
		PhoneRight pr=new PhoneRight();
		if(!StringUtils.isEmpty(pmJson)){
			JSONArray arry = JSONArray.fromObject(pmJson);
			
			Map<String, Object>result=new HashMap<String, Object>();
			Map<String, String>queryMap=new HashMap<String, String>();
			Map<String, Object>queryRepeat =new HashMap<String, Object>();
	
			for(int i=0;i<arry.size();i++){
				JSONObject json=arry.getJSONObject(i);

				queryMap.put("id", json.getString("id"));
				//调用接口.修改鼎尖yun上面的数据
				result=ProjectMApiRemoteServer.updatePhoneUse(queryMap);
				if(null!=result && "SUCC".equals(result.get("FLAG"))){
					/**
					 * 判断数据重复
					 */
					queryRepeat.put("repertId", json.getString("id"));
					boolean modifyFlag = false ;
					List<PhoneMember>pms=queryExecutor.execQuery(PhoneMember.MAPPER+".select", queryRepeat, PhoneMember.class);
					if(null!=pms && pms.size()>0){
						pm=pms.get(0);
						modifyFlag=true;
					}
					pr.setId(json.getString("rangeId"));			//权限id		
					pm.setId(json.getString("id"));
					pm.setPassword(json.getString("userKey"));
					pm.setOrgInterfaceId(json.getString("orgInterfaceId"));
					pm.setUserId(json.getString("userId"));
					pm.setLoginNumber(json.getString("regNumber"));
					pm.setPhoneRight(pr);
					pm.setEnable(PhoneEnableEnum.USE);
					pm.setHttpUrl(json.getString("httpUrl"));
					pm.setSpid(json.getString("spid"));
					pm.setPassWd(json.getString("passWord"));
					pm.setDefaultShowPhone(json.getString("showNumber"));
					pm.setShowPhone(json.getString("showNumber"));			
					pm.setPhoneType(json.getString("phoneType"));
					pm.setDefaultAnswerPhone(json.getString("showNumber"));
					pm.setNewPhone(json.getString("newPhone"));
					String isAllot=json.getString("isAllot");
					if(!StringUtils.isEmpty(isAllot) && "YES".equals(isAllot)){//如果分配过来的allot是空的
						pm.setIsAllot(CommonFlagEnum.YES);//针对于电信号码..该号码为计费号码
					}
					
					if("more".equals(json.getString("matchType").toString())){
						pm.setSetType(PhoneMemberEnum.SHAR);//批量分配时,都为共享模式
						pm.setAlias(json.getString("alias"));
						if(StringUtils.isEmpty(oldMember.getDescription())){
							pm.setDescription(json.getString("remark"));
						}
					}
					
					if(null!=pm.getOrg()){
						pm.setControlUnit(OrgUtils.getCUByOrg(pm.getOrg().getId()));
					}
					
					if(modifyFlag){
						/**
						 * 该数据从表单提交过来的
						 */
						pm.setOrg(oldMember.getOrg());
						pm.setOnlyUser(oldMember.getOnlyUser());
						pm.setMac(oldMember.getMac());
						pm.setSetType(oldMember.getSetType());
						pm.setAlias(oldMember.getAlias());
						pm.setDescription(oldMember.getDescription());
						pm.setUseType(oldMember.getUseType());
						pm.setUpdator(SystemUtil.getCurrentUser());
						this.phoneMemberDao.updateEntity(pm);
					}else{
						pm.setCreator(SystemUtil.getCurrentUser());
						pm.setCreateTime(new Date());
						this.phoneMemberDao.addEntity(pm);
					}
					matchResult.put("STATE", "SUCCESS");
					matchResult.put("MSG", "分配成功");
				}else{
					matchResult.put("STATE", "FAIL");
					matchResult.put("MSG", PhoneMemberUtil.getMsgByErrorKey(PhoneMemberUtil.OP_TYPE_ALLOT, (String)result.get("MSG")));
				}
			}	
		}else{
			matchResult.put("STATE", "FAIL");
			matchResult.put("MSG", "分配数据为空");
		}
		return matchResult;
	}

	@Override
	public void taskAutoCleanCurrUser() throws Exception {
		// TODO Auto-generated method stub
		try{
		Map<String,Object> cleanParam = new HashMap<String,Object>();
		cleanParam.put("clearAll", "clearAll");
		this.queryExecutor.executeUpdate("com.wuyizhiye.cmct.phone.dao.PhoneMemberDao.cleanCurrUser", cleanParam);
		}catch(Exception e){
			e.printStackTrace() ;
			log.error("定时任务 清理占用话机taskAutoCleanCurrUser："+e.getMessage());
			throw new  Exception(e);
		}
	}

	@Override
	public void addEntity(PhoneMember entity) {
		if(null!=entity.getOrg()){
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getOrg().getId()));
		}
		super.addEntity(entity);
	}

	@Override
	public void updateEntity(PhoneMember entity) {
		if(null!=entity.getOrg()){
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getOrg().getId()));
		}
		super.updateEntity(entity);
	}
}