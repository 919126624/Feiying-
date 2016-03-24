package com.wuyizhiye.cmct.phone.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.dao.PhoneDialDetailDao;
import com.wuyizhiye.cmct.phone.dao.PhoneSyncHistoryDao;
import com.wuyizhiye.cmct.phone.model.PhoneConfig;
import com.wuyizhiye.cmct.phone.model.PhoneDialDetail;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.model.PhoneSyncHistory;
import com.wuyizhiye.cmct.phone.service.PhoneDialDetailService;
import com.wuyizhiye.cmct.phone.util.FjCtCmctMemberUtil;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.cmct.phone.util.ProjectMApiRemoteServer;

/**
 * @ClassName PhoneDialDetailServiceImpl
 * @Description 话费明细service接口实现
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneDialDetailService")
@Transactional
public class PhoneDialDetailServiceImpl extends BaseServiceImpl<PhoneDialDetail> implements
	PhoneDialDetailService {

	private static Log log = LogFactory.getLog(PhoneDialDetailServiceImpl.class);
	
	@Autowired
	private PhoneDialDetailDao phoneDialDetailDao; 
	
	@Autowired
	private PhoneSyncHistoryDao phoneSyncHistoryDao ;
	
	@Override
	protected BaseDao getDao() {
		return phoneDialDetailDao;
	}
	
	@Override
	public Map<String, Object> syncCallList(Map<String, Object> param)
			throws Exception {
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		Map<String,Object> configParam = new HashMap<String,Object>();
		configParam.put("isEnable", CommonFlagEnum.YES.getValue());
		List<PhoneConfig> configList = queryExecutor.execQuery(PhoneConfig.MAPPER+".select", configParam, PhoneConfig.class);
		if(configList!=null && configList.size()>0){
			for(PhoneConfig config : configList){//同步所有组织
				Date startTime = null ;
				Date endTime = null ;
				//查找同步历史，查找出最后的同步时间作为本次开始同步时间
				Map<String,Object> syncHisParam = new HashMap<String,Object>();
				syncHisParam.put("syncOrgId", config.getOrgId());
				List<PhoneSyncHistory> syncHistoryList = queryExecutor.execQuery(PhoneSyncHistory.MAPPER+".select", syncHisParam, PhoneSyncHistory.class);
				if(syncHistoryList!=null && syncHistoryList.size() > 0){
					startTime = syncHistoryList.get(0).getEndTime();
				}else{
					//同步时间 默认从2013-07-01 00:00:00 开始同步
					startTime = DateUtil.convertStrToDate("2013-07-01 00:00:00", DateUtil.GENERAL_FORMHMS);
				}
				//一次同步一个月，设置结束日期不超过当前时间
				endTime = DateUtils.addMonths(startTime, 1);
				if(DateUtil.compareDate(DateUtil.getCurDate(), endTime) < 0){
					endTime = DateUtil.getCurDate();
				}
				
				//得到接口要求的格式参数
				String regEx = "[^0-9]";
				Pattern p = Pattern.compile(regEx);
				String sTimeStr = p.matcher(DateUtil.convertDateToStrHms(startTime)).replaceAll("").trim();
				String eTimeStr = p.matcher(DateUtil.convertDateToStrHms(endTime)).replaceAll("").trim();
				String sTime = sTimeStr.substring(0, sTimeStr.length() -2);
				String eTime = eTimeStr.substring(0, eTimeStr.length() -2);
				param.put("sTime", sTime);
				param.put("eTime", eTime);
				
				PhoneSyncHistory csh = new PhoneSyncHistory();
				csh.setUUID() ;
				csh.setPerson(SystemUtil.getCurrentUser());
				csh.setSyncOrgId(config.getOrgId());
				csh.setSyncTime(DateUtil.getCurDate());
				csh.setStartTime(startTime);
				csh.setEndTime(endTime);
				
				List<PhoneDialDetail> callList = new ArrayList<PhoneDialDetail>();
				param.put("orgInterfaceId", config.getOrgId());
				Date d1 = new Date();
				Map<String,Object> result = PhoneMemberUtil.getCallList(param,null);
				Date d2 = new Date();
				System.err.println("time cost 2:"+DateUtil.getDifferSecound(d2, d1));
				if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE").toString())){//成功
					Map<String,Object> pageData = (Map<String, Object>) result.get("list");
					for(String pageNo : pageData.keySet()){
						JSONArray jsonArr = JSONArray.fromObject(pageData.get(pageNo));
						for(int i = 0 ; i < jsonArr.size() ;i ++){
							JSONObject jsonObj = jsonArr.getJSONObject(i);
							String startTimeStr = jsonObj.getString("start_time") == null ? null : jsonObj.getString("start_time") ;
							PhoneDialDetail cd = new PhoneDialDetail() ;
							cd.setCallId(jsonObj.getString("call_id"));
							cd.setCostId(jsonObj.getString("billcallid"));
							cd.setInfoNumber(jsonObj.getString("show_Phone"));
							cd.setBusType(jsonObj.getString("service_Name"));
							cd.setOutherNumber(jsonObj.getString("user_Phone"));
							cd.setPeriod(StringUtils.isEmpty(startTimeStr) ? null : startTimeStr.substring(0, 7));
							cd.setStartTime(StringUtils.isEmpty(startTimeStr) ? null : DateUtil.convertStrToDate(startTimeStr, DateUtil.GENERAL_FORMHMS));
							cd.setEndTime(jsonObj.getString("end_time") == null ? null : DateUtil.convertStrToDate(jsonObj.getString("end_time")+"", DateUtil.GENERAL_FORMHMS));
							cd.setCallDuration(jsonObj.getString("call_times"));
							cd.setRate(jsonObj.getString("price") == null ? null : Double.valueOf(jsonObj.getString("price")));
							cd.setCallCost(jsonObj.getString("fees") == null ? null : Double.valueOf(jsonObj.getString("fees")));
							cd.setSyncId(csh.getId());
							callList.add(cd);
						}
					}
					csh.setSyncCount(callList.size());
					Date d3= new Date();
					System.err.println("time cost 3:"+DateUtil.getDifferSecound(d3, d2));
					phoneSyncHistoryDao.addEntity(csh);
					if(callList.size() > 0){
						//去除重复
						Map<String,Object> callIdMap = new HashMap<String,Object>();
						List<PhoneDialDetail> newList = new ArrayList<PhoneDialDetail>();
						for(PhoneDialDetail detail : callList){
							if(!StringUtils.isEmpty(detail.getCallId()) && !callIdMap.containsKey(detail.getCallId())){
								newList.add(detail);
								callIdMap.put(detail.getCallId(), detail.getCallId());
							}
						}
						phoneDialDetailDao.addBatch(newList);
					}
					Date d4= new Date();
					System.err.println("time cost 4:"+DateUtil.getDifferSecound(d4, d3));
					rtnMap.put("STATE", "SUCCESS");
					rtnMap.put("syncCount", csh.getSyncCount());
				}else{//失败
					rtnMap.put("STATE", "FAIL");
					rtnMap.put("MSG", result.get("MSG"));
				}
			}
		}
		return rtnMap ;
	}

	@Override
	public Map<String, Object> syncCallListDay(Map<String, Object> param)
			throws Exception {
		
		Map<String,Object> rtnMap = new HashMap<String,Object>();
//		Map<String,Object> configParam = new HashMap<String,Object>();
		String syncOrgId=param.get("syncOrgId").toString();
		
		String start="";
        String end="";
//		if(configList!=null && configList.size()>0){
//			for(PhoneConfig config : configList){//同步所有组织
				
				String syncTime = param.get("syncTime") == null ? DateUtil.convertDateToStr(DateUtil.getCurDate()) : param.get("syncTime").toString();//按天同步,
				
				String startTimeByPeriod=(String) param.get("periond");//整月同步,传进来的是个月份
				
				if(!StringUtils.isEmpty(startTimeByPeriod)){//按月同步
					start=startTimeByPeriod+"-01";
					end=getAfter(getMonthEnd(start));
				}else{										//按天同步
					start=syncTime;
					end=getAfter(syncTime);
				}
				
				//查找同步历史，根据传进来的需要同步的日期去数据库查找，如果有数据，则以前同步过。操作即为重新同步，修改记录表。否则即为同步，增加记录。；
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				Map<String,Object> syncHisParam = new HashMap<String,Object>();
				
				if(!"".equals(start)){						//
					syncHisParam.put("startTime", df.parse(start));
	        	}
				
				syncHisParam.put("syncOrgId", syncOrgId);
				List<PhoneSyncHistory> syncHistoryList = queryExecutor.execQuery(PhoneSyncHistory.MAPPER+".select", syncHisParam, PhoneSyncHistory.class);
				String oldSyncId = "" ;
				if(syncHistoryList!=null && syncHistoryList.size()>0){//重新同步
					oldSyncId = syncHistoryList.get(0).getId();
				}
				Date startTime=null;
				Date endTime=null;
				if(!StringUtils.isEmpty(startTimeByPeriod)){
					startTime = DateUtil.convertStrToDate(start + " 00:00:00",DateUtil.GENERAL_FORMHMS) ;
					endTime = DateUtil.convertStrToDate( end+ " 00:00:00",DateUtil.GENERAL_FORMHMS) ;
				}else{
					startTime = DateUtil.convertStrToDate(syncTime + " 00:00:00",DateUtil.GENERAL_FORMHMS) ;
					endTime = DateUtil.getNextDay(startTime);
				}
								
				//得到接口要求的格式参数
				String regEx = "[^0-9]";
				Pattern p = Pattern.compile(regEx);
				String sTimeStr = p.matcher(DateUtil.convertDateToStrHms(startTime)).replaceAll("").trim();
				String eTimeStr = p.matcher(DateUtil.convertDateToStrHms(endTime)).replaceAll("").trim();
				String sTime = sTimeStr.substring(0, sTimeStr.length() -2);
				String eTime = eTimeStr.substring(0, eTimeStr.length() -2);
				param.put("sTime", sTime);
				param.put("eTime", eTime);
				
				PhoneSyncHistory csh = new PhoneSyncHistory();;//同步，获取最新的一条记录
				csh.setUUID() ;
				csh.setPerson(SystemUtil.getCurrentUser());
				csh.setSyncOrgId(syncOrgId);
				csh.setStartTime(startTime);
				csh.setEndTime(endTime);
				
				/*if(!StringUtils.isEmpty(startTimeByPeriod)){
					csh.setEndTime(endTime);
				}*/
				
				List<PhoneDialDetail> addList = new ArrayList<PhoneDialDetail>();
				//param.put("orgInterfaceId", syncOrgId);
				Date d1 = new Date();
				Map<String,Object> result = PhoneMemberUtil.getCallList(param,null);
				Date d2 = new Date();
				System.err.println("time cost 2:"+DateUtil.getDifferSecound(d2,d1));
				if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE").toString())){//成功
					Map<String,Object> pageData = (Map<String, Object>) result.get("list");
					for(String pageNo : pageData.keySet()){
						JSONArray jsonArr = JSONArray.fromObject(pageData.get(pageNo));
						for(int i = 0 ; i < jsonArr.size() ;i ++){
							JSONObject jsonObj = jsonArr.getJSONObject(i);
							PhoneDialDetail cd = new PhoneDialDetail() ;
							String callId = jsonObj.getString("call_id") ;
							String startTimeStr = jsonObj.getString("start_time") == null ? null : jsonObj.getString("start_time") ;
							if(!StringUtils.isEmpty(callId)){
								/*
								queryMap.put("callId", callId);
								List<PhoneDialDetail> detailList = queryExecutor.execQuery(PhoneDialDetail.MAPPER + ".selectByCond", queryMap, PhoneDialDetail.class);
								if(detailList!=null && detailList.size() > 0){
									cd = detailList.get(0);
									modifyFlag = true ;
								}
								*/
							}
							cd.setCallId(jsonObj.getString("call_id"));
							cd.setCostId(jsonObj.getString("billcallid"));
							cd.setInfoNumber(jsonObj.getString("show_Phone"));
							cd.setBusType(jsonObj.getString("service_Name"));
							cd.setOutherNumber(jsonObj.getString("user_Phone"));
							cd.setPeriod(StringUtils.isEmpty(startTimeStr) ? null : startTimeStr.substring(0, 7));
							cd.setStartTime(StringUtils.isEmpty(startTimeStr) ? null : DateUtil.convertStrToDate(startTimeStr, DateUtil.GENERAL_FORMHMS));
							cd.setEndTime(jsonObj.getString("end_time") == null ? null : DateUtil.convertStrToDate(jsonObj.getString("end_time")+"", DateUtil.GENERAL_FORMHMS));
							cd.setCallDuration(jsonObj.getString("call_times"));
							cd.setRate(jsonObj.getString("price") == null ? null : Double.valueOf(jsonObj.getString("price")));
							cd.setCallCost(jsonObj.getString("fees") == null ? null : Double.valueOf(jsonObj.getString("fees")));
							cd.setSyncId(csh.getId());
							addList.add(cd);
						}
					}
					Date d3 = new Date();
					System.err.println("time cost 3:"+DateUtil.getDifferSecound(d3,d2));
					
					if(addList.size() > 0 ){
						//去除重复
						Map<String,Object> callIdMap = new HashMap<String,Object>();
						List<PhoneDialDetail> newList = new ArrayList<PhoneDialDetail>();
						for(PhoneDialDetail detail : addList){
							if(!StringUtils.isEmpty(detail.getCallId()) && !callIdMap.containsKey(detail.getCallId())){
								newList.add(detail);
								callIdMap.put(detail.getCallId(), detail.getCallId());
							}
						}
						int unit = 500 ;
						int tsize = newList.size() ;
						int ap = tsize / unit == 0  ? tsize / unit :(tsize / unit + 1) ;
						if(tsize <= unit){
							ap = 1 ;
						}
						for(int k = 0 ; k < ap ; k ++){
							List<PhoneDialDetail> tempList = null ;
							if(k == ap -1 ){
								tempList = newList.subList(k*unit, tsize);
							}else{
								tempList = newList.subList(k*unit, (k+1)*unit);
							}
							if(tempList!=null){
								phoneDialDetailDao.addBatch(tempList);
							}
						}
						//phoneDialDetailDao.addBatch(newList);
						if(!StringUtils.isEmpty(oldSyncId)){
							//删除旧的数据
							phoneDialDetailDao.deleteBySyncId(oldSyncId);
							//删除旧同步记录
							phoneSyncHistoryDao.deleteById(oldSyncId);
						}
						csh.setSyncCount(newList.size());
						csh.setSyncTime(DateUtil.getCurDate());
						phoneSyncHistoryDao.addEntity(csh);//增加记录
					}
					
					Date d4 = new Date();
					System.err.println("time cost 4:"+DateUtil.getDifferSecound(d4,d3));
					rtnMap.put("STATE", "SUCCESS");
					rtnMap.put("syncCount", csh.getSyncCount()==null?"0":csh.getSyncCount());
				}else{//失败
					rtnMap.put("STATE", "FAIL");
					rtnMap.put("MSG", result.get("MSG"));
				}
	//		}
	//	}
		return rtnMap ;
	}

	@Override
	public void taskAutoSyncCallList() throws Exception{
		try{
			Map<String,Object> param = new HashMap<String,Object>();
			//取当天的上一天同步
			String syncTime = DateUtil.convertDateToStr(DateUtil.getPrevDay(DateUtil.getCurDate()));
			param.put("syncTime", syncTime);
			
			Map<String,String> configParam = new HashMap<String,String>();
			List<PhoneConfig>pcs=new ArrayList<PhoneConfig>();
			//根据系统参数从鼎尖yun上读取核算渠道,本地核算渠道表已经无效  by lxl 14.5.13 
			configParam.put("customerId", ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID));
			Map<String, String> result=ProjectMApiRemoteServer.getCustomerByOrgId(configParam);//获取核算渠道,一个客户下可能有多个核算渠道
			if(result!=null && "SUCCESS".equals(result.get("STATE"))){
				String strConfig=result.get("pcs");
				JSONArray jsonArr=JSONArray.fromObject(strConfig);
				if(null!=jsonArr && jsonArr.size()>0){
					for(int i=0;i<jsonArr.size();i++){
						JSONObject jsonObj=jsonArr.getJSONObject(i);
						PhoneConfig pc=new PhoneConfig();
						pc.setConfigName(jsonObj.getString("configName"));
						pc.setOrgId(jsonObj.getString("orgId"));
						pc.setPartners(jsonObj.getString("partners"));
						pc.setOrgKey(jsonObj.getString("orgKey"));
						pc.setGetCallUrl(jsonObj.getString("getCallUrl"));
						pc.setAddUrl(jsonObj.getString("addUrl"));//对应的是电信的环境地址
						pc.setModifyUrl(jsonObj.getString("modifyUrl"));//对应的是电信的spid
						pcs.add(pc);
					}
				}
				
			}
			for(PhoneConfig pc:pcs){
				if(!StringUtils.isEmpty(pc.getPartners()) && "TTEN".equals(pc.getPartners())){//定时同步铁通账单
					param.put("syncOrgId", pc.getOrgId());
					param.put("syncOrgKey", pc.getOrgKey());
					param.put("getCallUrl", pc.getGetCallUrl());
					this.syncCallListDay(param);
				}else if(!StringUtils.isEmpty(pc.getPartners()) && "HW".equals(pc.getPartners())){//定时同步电信账单
					param.put("syncOrgId", pc.getOrgId());
					param.put("syncOrgKey", pc.getOrgKey());
					this.syncDxCallListDay(param);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace() ;
			log.error("定时任务同步话单异常 taskAutoSyncCallList："+e.getMessage());
			throw new  Exception(e);
		}
	}
	
	@Override
	public Map<String, Object> syncDxCallListDay(Map<String, Object> param)
			throws Exception {
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");		
		Date startTime = (Date) sf.parse((String) param.get("syncTime"));
		if (startTime == null) {
			rtnMap.put("STATE", "FAIL");
			rtnMap.put("MSG", "时间为空.....");
			return rtnMap;
		}
		
		String period="";
		if(!StringUtils.isEmpty(param.get("syncTime").toString())){
			period=param.get("syncTime").toString().substring(0,7);
		}
		
		if(null==param.get("syncOrgId")){
			rtnMap.put("STATE", "FAIL");
			rtnMap.put("MSG", "核算渠道为空");
			return rtnMap;
		}
		Date endTime=DateUtil.getNextDay(startTime);
		JSONArray billList = new JSONArray();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object>pmParm=new HashMap<String, Object>();
		pmParm.put("phoneType", "HW");//查找出所有运营商为华为的坐席
		pmParm.put("orgInterfaceId", param.get("syncOrgId").toString());
		//pmParm.put("isAllot", "YES");
		List<PhoneMember>pms=queryExecutor.execQuery(PhoneMember.MAPPER+".select", pmParm, PhoneMember.class);
		if(null!=pms && pmParm.size()>0){
			/**
			 * 查找是否以前同步过
			 */
			Map<String,Object> syncHisParam = new HashMap<String,Object>();
			if(param.get("syncOrgId")==null){
				rtnMap.put("STATE", "FAIL");
				rtnMap.put("MSG", "核算渠道id为空");
				return rtnMap;
			}
			syncHisParam.put("startTime", startTime);
			syncHisParam.put("syncOrgId", param.get("syncOrgId").toString());
			List<PhoneSyncHistory> syncHistoryList = queryExecutor.execQuery(PhoneSyncHistory.MAPPER+".select", syncHisParam, PhoneSyncHistory.class);
			String oldSyncId = "" ;
			if(syncHistoryList!=null && syncHistoryList.size()>0){//重新同步
				oldSyncId = syncHistoryList.get(0).getId();
			}
			
			PhoneSyncHistory csh = new PhoneSyncHistory();;//同步，获取最新的一条记录
			csh.setUUID() ;
			csh.setPerson(SystemUtil.getCurrentUser());
			csh.setSyncOrgId(param.get("syncOrgId").toString());
			csh.setStartTime(startTime);
			csh.setEndTime(endTime);
		
			List<PhoneDialDetail> addList = new ArrayList<PhoneDialDetail>();
			
			for(PhoneMember pm:pms){
				if(null!=pm.getIsAllot() && "YES".equals(pm.getIsAllot().getValue())){					
					pm.setStartTime(startTime);
					pm.setEndTime(endTime);
					result = FjCtCmctMemberUtil.imsBill(pm);
					if (result.get("STATE").equals("SUCCESS")) { 
						Map<String, Object> dataListStr = (Map<String, Object>) result.get("respMap");
						billList.addAll(JSONArray.fromObject(dataListStr.get("dataList")));
					}else{
						rtnMap.put("STATE", "FAIL");
						rtnMap.put("MSG", result.get("MSG"));
						return rtnMap;
					}
				}
			}
			
			if(null!=billList&&billList.size()>0){
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < billList.size(); i++) {
					PhoneDialDetail ppdd = new PhoneDialDetail();
					JSONObject jsonObj = billList.getJSONObject(i);
					ppdd.setCallId(jsonObj.getString("Sessionid"));// 返回的sessionID
					ppdd.setCostId(jsonObj.getString("ChargeNbr"));// 计费号码
					ppdd.setOutherNumber(jsonObj.getString("CalledNbr"));// 被呼叫号码-->对方号码
					
					String callerNbr=jsonObj.getString("CallerNbr");
					if(callerNbr.startsWith("86")){
						callerNbr =	"0"+callerNbr.substring(2, callerNbr.length());
					}
					ppdd.setInfoNumber(callerNbr);// 主叫号码
					ppdd.setStartTime(sdf2.parse(jsonObj.getString("StartTime")));// 通话起始时间
					ppdd.setEndTime(sdf2.parse(jsonObj.getString("EndTime")));// 通话起始时间
					ppdd.setCallDuration(jsonObj.getString("Duration")); // 通话时长
					ppdd.setBusType(jsonObj.getString("BillSubtype"));
					ppdd.setRate(jsonObj.getString("Points")==null ?0:Double.valueOf(jsonObj.getString("Points")));//电信的点数存进费率
					ppdd.setSyncId(csh.getId());// 此处相当与更新了对象的历史id				
					ppdd.setPeriod(period);
					addList.add(ppdd);
				}
			}else{
				rtnMap.put("STATE", "FAIL");
				rtnMap.put("MSG", "没有查询出数据");
				return rtnMap;
			}
			
			if(addList.size() > 0 ){
				//去除重复
				Map<String,Object> callIdMap = new HashMap<String,Object>();
				List<PhoneDialDetail> newList = new ArrayList<PhoneDialDetail>();
				for(PhoneDialDetail detail : addList){
					if(!StringUtils.isEmpty(detail.getCallId()) && !callIdMap.containsKey(detail.getCallId())){
						newList.add(detail);
						callIdMap.put(detail.getCallId(), detail.getCallId());
					}
				}
				int unit = 500 ;
				int tsize = newList.size() ;
				int ap = tsize / unit == 0  ? tsize / unit :(tsize / unit + 1) ;
				if(tsize <= unit){
					ap = 1 ;
				}
				for(int k = 0 ; k < ap ; k ++){
					List<PhoneDialDetail> tempList = null ;
					if(k == ap -1 ){
						tempList = newList.subList(k*unit, tsize);
					}else{
						tempList = newList.subList(k*unit, (k+1)*unit);
					}
					if(tempList!=null){
						phoneDialDetailDao.addBatch(tempList);
					}
				}
				
				if(!StringUtils.isEmpty(oldSyncId)){
					//删除旧的数据
					phoneDialDetailDao.deleteBySyncId(oldSyncId);
					//删除旧同步记录
					phoneSyncHistoryDao.deleteById(oldSyncId);
				}
				csh.setSyncCount(newList.size());
				csh.setSyncTime(DateUtil.getCurDate());
				phoneSyncHistoryDao.addEntity(csh);//增加记录
				rtnMap.put("STATE", "SUCCESS");
				rtnMap.put("syncCount", csh.getSyncCount() == null ? 0 : csh.getSyncCount());
				return rtnMap;
			}else{
				rtnMap.put("STATE", "FAIL");
				rtnMap.put("MSG", "没有查询出数据");
				return rtnMap;
			}
		}else{
			rtnMap.put("STATE", "没有查到电信的坐席号码");
		}
		return rtnMap;
	}
	
	@Override
	public Org getOrgByType(String number) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("orgType", number);
		List<Org>  olist=this.phoneDialDetailDao.getOrgByType(param);
		Org org=null;
		/**
		 * 取级别 最小的 组织
		 */
		if(null != olist && olist.size()>0){
			for(int i=0;i<olist.size();i++){
				Org o=olist.get(i);
				if(i == 0){
					org=o;
				}else{
					if(o.getLevel()<org.getLevel()){
						org=o;
					}
				}
			}
		}
		return org;
	}
	
	/** 
	* 获得指定日期的后一天 
	* @param specifiedDay 
	* @return 
	*/ 
	public static String getAfter(String specifiedDay){ 
	Calendar c = Calendar.getInstance(); 
	Date date=null; 
	try { 
	date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay); 
	} catch (ParseException e) { 
	e.printStackTrace(); 
	} 
	c.setTime(date); 
	int day=c.get(Calendar.DATE); 
	c.set(Calendar.DATE,day+1); 

	String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()); 
	return dayAfter; 
	}
	/**
	 * 获取一个月 的最后一天
	 * @param str
	 * @return
	 */
	public String getMonthEnd(String str){
		 Calendar c = Calendar.getInstance();
		  SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM");
		  try {
			c.setTime(format.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		  int MaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		  c.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), MaxDay, 23, 59, 59);
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   return sdf.format(c.getTime()); 
	}

}
