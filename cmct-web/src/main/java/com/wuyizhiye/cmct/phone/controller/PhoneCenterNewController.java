package com.wuyizhiye.cmct.phone.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.enums.DialResultEnum;
import com.wuyizhiye.cmct.phone.enums.DialTypeEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneMemberEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneStateEnum;
import com.wuyizhiye.cmct.phone.model.PhoneDialLog;
import com.wuyizhiye.cmct.phone.model.PhoneDialRecord;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.model.PhoneNumber;
import com.wuyizhiye.cmct.phone.service.PhoneDialLogService;
import com.wuyizhiye.cmct.phone.service.PhoneDialRecordService;
import com.wuyizhiye.cmct.phone.service.PhoneMemberService;
import com.wuyizhiye.cmct.phone.util.DialRecordUtil;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName PhoneCenterNewController
 * @Description 话务中心控制器 后期2.0版本
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phonenew/*")
public class PhoneCenterNewController extends BaseController {
	
	private static Logger logger =Logger.getLogger(PhoneCenterNewController.class); 
	
	@Autowired
	private PhoneMemberService phoneMemberService ;
	
	@Autowired
	private PhoneDialRecordService phoneDialRecordService ;
	
	@Autowired
	private PhoneDialLogService phoneDialLogService ;
	
	/**
	 * 呼叫中心首页
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("index")
	public String index(ModelMap modelMap){
		
		
		/**
		 * cmct平台选择电话号码的方式 ,by lxl 14.2.24
		 */
		modelMap.put("cmctUsePhone", ParamUtils.getParamValue(PhoneMemberUtil.CMCT_USE_PHONE));
		
	//	return "cmct/phone/phoneCenter" ;
		return "cmct/merage/meragePhoneCenter" ;
	}
	
	/**
	 * 获取登录人呼叫信息
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="getUserInfo")
	public void getUserInfo(HttpServletResponse response) throws Exception{
		if(SystemUtil.getCurrentUser() == null || StringUtils.isEmpty(SystemUtil.getCurrentUser().getId())){
			getOutputMsg().put("STATE", "EXCEPTION");
			getOutputMsg().put("NOLOGIN", "请先登录系统");
		}else{
			//查找登录人是否之前有选择的路线
			PhoneMember callSet = null ;
			Map<String,Object> condMap = new HashMap<String,Object>();
			condMap.put("currUserId", SystemUtil.getCurrentUser().getId());
			condMap.put("enable", CommonFlagEnum.YES);
			List<PhoneMember> callSetList = queryExecutor.execQuery(PhoneMember.MAPPER+".select", condMap, PhoneMember.class);
			if(callSetList!=null && callSetList.size()>0){
				callSet = callSetList.get(0);
				//查出计费号码
				condMap.clear();
				condMap.put("phoneType", "HW");
				condMap.put("isAllot", "YES");
				List<PhoneMember>costNumbers=queryExecutor.execQuery(PhoneMember.MAPPER+".select", condMap, PhoneMember.class);
				if(null!=costNumbers && costNumbers.size()>0){
					if(!StringUtils.isEmpty(callSet.getPhoneType()) && "HW".equals(callSet.getPhoneType()) && costNumbers.get(0).getOrgInterfaceId().equals(callSet.getOrgInterfaceId())){
						callSet.setUserId(costNumbers.get(0).getUserId());
						callSet.setPassword(costNumbers.get(0).getPassword());
					}
				}
			}else{
				//如果没有当前使用的，则查找当前人的专用路线
				condMap.clear();
				condMap.put("onlyUserId", SystemUtil.getCurrentUser().getId());
				condMap.put("enable", CommonFlagEnum.YES);
				callSetList = queryExecutor.execQuery(PhoneMember.MAPPER+".select", condMap, PhoneMember.class);
				if(callSetList!=null && callSetList.size()>0){
					callSet = callSetList.get(0);
					//查出计费号码
					condMap.clear();
					condMap.put("phoneType", "HW");
					condMap.put("isAllot", "YES");
					List<PhoneMember>costNumbers=queryExecutor.execQuery(PhoneMember.MAPPER+".select", condMap, PhoneMember.class);
					if(null!=costNumbers && costNumbers.size()>0){
						if(!StringUtils.isEmpty(callSet.getPhoneType()) && "HW".equals(callSet.getPhoneType()) && costNumbers.get(0).getOrgInterfaceId().equals(callSet.getOrgInterfaceId())){
							callSet.setUserId(costNumbers.get(0).getUserId());
							callSet.setPassword(costNumbers.get(0).getPassword());
						}
					}
				}else{
					//没有专用的，则选择共享线路是绑定了当前MAC地址的
					String signature = SystemUtil.getCurrentSignature() ;
					if(!StringUtils.isEmpty(signature)){
						condMap.clear();
						condMap.put("macLogin",signature);
						condMap.put("enable", CommonFlagEnum.YES);
						condMap.put("noCurrUser", CommonFlagEnum.YES);
						callSetList = queryExecutor.execQuery(PhoneMember.MAPPER+".select", condMap, PhoneMember.class);
						if(callSetList!=null && callSetList.size()>0){
							callSet = callSetList.get(0);
							//查出计费号码
							condMap.clear();
							condMap.put("phoneType", "HW");
							condMap.put("isAllot", "YES");
							List<PhoneMember>costNumbers=queryExecutor.execQuery(PhoneMember.MAPPER+".select", condMap, PhoneMember.class);
							if(null!=costNumbers && costNumbers.size()>0){
								if(!StringUtils.isEmpty(callSet.getPhoneType()) && "HW".equals(callSet.getPhoneType()) && costNumbers.get(0).getOrgInterfaceId().equals(callSet.getOrgInterfaceId())){
									callSet.setUserId(costNumbers.get(0).getUserId());
									callSet.setPassword(costNumbers.get(0).getPassword());
								}
							}
						}
					}
				}
			}
			if(callSet!=null && !StringUtils.isEmpty(callSet.getId())){
				getOutputMsg().put("STATE", "SUCCESS");
				//callSet.setOrgInterfaceId(CallCenterUtil.getSystemParam(CallCenterUtil.CALL_ORGID));
				getOutputMsg().put("callSet", callSet);
			}else{
				//未找到线路，用户自己去选择
				getOutputMsg().put("STATE", "FAIL");
			}
			//by lxl 判断当前选择线路是否是pc端
			getOutputMsg().put("isNotPc", isMobileDevice());
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 呼叫记录
	 * @param response
	 */
	@RequestMapping(value="record")
	public void record(HttpServletResponse response){
		
		String toPhone = getString("toPhone","");
		String toName = getString("toName","");
		String callBillId = getString("callBillId","");
		String callLineId = getString("callLineId","");
		String time = getString("time","");
		String currShowPhone = getString("currShowPhone","");
		String objectId = getString("objectId","");
		
	//	String callSessionId=getString("sessionId");//只针对于电信号码有效
		try{
			//查找呼叫线路
			PhoneMember callSet = phoneMemberService.getEntityById(callLineId);
			PhoneDialRecord cr = new PhoneDialRecord();
			cr.setUUID();
			cr.setCallId(SystemUtil.getCurrentUser().getId());
			cr.setControlUnit(SystemUtil.getCurrentControlUnit());
			cr.setCallName(SystemUtil.getCurrentUser().getName());
			cr.setCallType(DialTypeEnum.WORK);
			cr.setCallTime(new Date());
			cr.setFromPhone(callSet == null ? null : callSet.getDefaultAnswerPhone());
			cr.setToPhone(toPhone);
			cr.setToName(toName);
			cr.setCurrShowPhone(currShowPhone);
			cr.setCallBillId(callBillId);//电信铁通共存一个通话id
			cr.setCallResult(DialResultEnum.C_122);
			cr.setAtime(StringUtils.isEmpty(time)?null:Integer.valueOf(time));
			cr.setSuffix(DialRecordUtil.getSuffix());
			if(!StringUtils.isEmpty(objectId)){
				cr.setObjectId(objectId);
			}
	//		cr.setSessionId(callSessionId);
			
			phoneDialRecordService.addEntity(cr);
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("ID", cr.getId());
			getOutputMsg().put("MSG", "保存成功");
		}catch(Exception e){
			e.printStackTrace();
			logger.debug(e.getStackTrace());
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录呼叫记录出现异常，请联系管理员。"+e.getMessage());
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 呼叫记录
	 * @param response
	 */
	@RequestMapping(value="recordUpdate")
	public void recordUpdate(HttpServletResponse response){
		String recordId = getString("recordId","");
		String callDur = getString("callDur","");
		String callResult = getString("callResult","");
		String remark = getString("remark","");
		String time = getString("time","");
		try{
			if(StringUtils.isEmpty(recordId)){
				return ;
			}
			//查找呼叫人
//			PhoneDialRecord cr = phoneDialRecordService.getEntityById(recordId);
			Map<String, Object>param=new HashMap<String, Object>();
			param.put("id", recordId);
			param.put("suffix", DialRecordUtil.getSuffix());
			PhoneDialRecord cr =queryExecutor.execOneEntity(PhoneDialRecord.MAPPER+".select", param, PhoneDialRecord.class);
			
			if(!StringUtils.isEmpty(callDur)){
				cr.setCostTime(Integer.valueOf(callDur));
			}
			if(!StringUtils.isEmpty(callResult)){
				cr.setCallResult(DialResultEnum.valueOf(callResult));
			}
			if(StringUtils.isEmpty(cr.getRemark())){
				cr.setRemark(remark);
			}else{
				cr.setRemark(cr.getRemark()+"|"+remark);
			}
			cr.setBtime(StringUtils.isEmpty(time) ? null : Integer.valueOf(time));
			cr.setSuffix(DialRecordUtil.getSuffix());
			phoneDialRecordService.updateEntity(cr);
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "保存成功");
		}catch(Exception e){
			logger.debug(e.getStackTrace());
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录呼叫记录（recordUpdate）出现异常，请联系管理员。"+e.getMessage());
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 切换话机号码
	 * @param response
	 */
	@RequestMapping(value="switchShowPhone")
	public void switchShowPhone(HttpServletResponse response){
		String phoneNo = getString("phoneNo","");
		String callPersonId = getString("callPersonId","");
		try{
			PhoneMember cs = phoneMemberService.getEntityById(callPersonId);
			cs.setUpdator(SystemUtil.getCurrentUser());
			cs.setDefaultShowPhone("HIDE".equals(phoneNo)?null:phoneNo);
			phoneMemberService.updateEntity(cs);
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "保存成功");
		}catch(Exception e){
			e.printStackTrace();
			logger.debug(e.getStackTrace());
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "切换去电号码（switchShowPhone）出现异常，请联系管理员。"+e.getMessage());
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 查询最新呼叫话单id
	 * @param response
	 */
	@RequestMapping(value="selectCallBillId")
	public void selectCallBillId(HttpServletResponse response){
		try {
			String billId = PhoneMemberUtil.getCallBillId();
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("CALLBILLID", billId);
		} catch (Exception e) {
			logger.debug(e.getStackTrace());
			e.printStackTrace();
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "生成最新呼叫话单ID（selectCallBillId）出现异常，请联系管理员。"+e.getMessage());
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 记录呼叫日志
	 * @param response
	 */
	@RequestMapping(value="addLog")
	public void addLog(HttpServletResponse response){
		String toName = getString("toName","");
		String toPhone = getString("toPhone","");
		String code = getString("code","");
		String rtnDesc = getString("rtnDesc","");
		String showPhone = getString("showPhone","");
		PhoneDialLog log = new PhoneDialLog();
		log.setUUID() ;
		log.setPerson(getCurrentUser());
		log.setCallTime(new Date());
		log.setToName(toName);
		log.setToPhone(toPhone);
		log.setCode(code);
		log.setRtnDesc(rtnDesc);
		log.setShowPhone(showPhone);
		phoneDialLogService.addEntity(log);
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 选择线路
	 * @param response
	 * @return
	 */
	@RequestMapping(value="selectLine")
	public String selectLine(ModelMap modelMap,HttpServletResponse response){
		modelMap.put("setTypes", PhoneMemberEnum.values());
		modelMap.put("personId", getCurrentUser().getId());
			
		/**
		 * 带出默认组织
		 */
		modelMap.put("org", SystemUtil.getCurrentOrg());
		//当前登录MAC
		String signature =SystemUtil.getCurrentSignature();
		modelMap.put("mac", signature);
		return "cmct/phone/selectPhoneLine" ;
	}
	
	/**
	 * 选择线路 
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="selectLineData")
	public void selectLineData(Pagination<PhoneMember> pagination,HttpServletResponse response) throws Exception{
		Map<String,String> param = getParamMap();
		Map<String,Object> params = new HashMap<String, Object>();
		Set<String> keys = param.keySet();
		for(String key : keys){
			params.put(key, param.get(key));
		}
		boolean findFlag = false ;
		String orgId = param.get("orgId");
		if(StringUtils.isEmpty(orgId)){
			//如果组织为空，则默认过滤为本组织的上级组织，组织隔离（允许选择上级部门的设置）
			String currLongNumber = SystemUtil.getCurrentOrg().getLongNumber() ;
			String longNumber = currLongNumber ;
			if(currLongNumber.indexOf("!")!=-1){
				longNumber = longNumber.substring(0,currLongNumber.lastIndexOf("!"));
			}
			params.put("longNumber", longNumber);
			findFlag = true ;
		}
		/* update by li.biao 去掉任何参数，界面控制 又更新一次 2014-7-3 12:17:07 haibin要求的
		//MAC地址隔离
		String signature =SystemUtil.getCurrentSignature();
		params.put("mac", signature);
		//选择共享 或者专用人为自己的线路
		params.put("onlyUserId", SystemUtil.getCurrentUser().getId());
		*/
		this.putControlUnitIdToMap(params);
		pagination = queryExecutor.execQuery(PhoneMember.MAPPER + ".selectLine", pagination, params);
		if(findFlag && (pagination==null || pagination.getItems() == null || pagination.getItems().size() == 0)){
			params.put("longNumber", null);
			pagination = queryExecutor.execQuery(PhoneMember.MAPPER + ".selectLine", pagination, params);
		}
		//查出电信号码的计费号码.根据allot来区分,重新查一次
		params.clear();
		params.put("phoneType", "HW");
		params.put("isAllot", "YES");
		List<PhoneMember>isCostNums= queryExecutor.execQuery(PhoneMember.MAPPER + ".selectLine", params, PhoneMember.class);
		List<PhoneMember>pms=pagination.getItems();
		if(null!=pms && pms.size()>0){
			for(PhoneMember pm:pms){
				if(null!=isCostNums && isCostNums.size()>0){
					if((!StringUtils.isEmpty(pm.getPhoneType()))&& "HW".equals(pm.getPhoneType()) && pm.getOrgInterfaceId().equals(isCostNums.get(0).getOrgInterfaceId())){
						pm.setUserId(isCostNums.get(0).getUserId());//设置当前的uesrId为计费号码的userId
						pm.setPassword(isCostNums.get(0).getPassword());//
						
					}
				}
			}
		}
		outPrint(response, JSONObject.fromObject(pagination, getSelectLineJsonConfig()));
	}
	
	/**
	 * 查询历史选择的三条线路,根据最后修改时间和最后修改人来查
	 * @return
	 */
	@RequestMapping(value="selectLineDataByLastThree")
	public void selectLineDataByLastThree(Pagination<PhoneMember> pagination,HttpServletResponse response){
		Map<String, Object>param=new HashMap<String, Object>();
		param.put("personId", SystemUtil.getCurrentUser().getId());
		List<PhoneMember>pms=queryExecutor.execQuery(PhoneMember.MAPPER+".selectLineDataByLastThree", param, PhoneMember.class);
		if(null!=pms && pms.size()>0){
			getOutputMsg().put("STATE", "SUCCESS");
			if(pms.size()>3){
				getOutputMsg().put("members", pms.subList(0, 3));
			}else{
				getOutputMsg().put("members", pms);
			}						
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(),getSelectLineJsonConfig()).toString());
	}
	
	public JsonConfig getSelectLineJsonConfig(){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(key !=null && "onlineTime".equals(key)){
					if(value!=null && value instanceof Date){
						return sdf2.format(value);
					}
				}
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		config.registerJsonValueProcessor(PhoneMemberEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof PhoneMemberEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((PhoneMemberEnum)value).getName());
					json.put("value", ((PhoneMemberEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof PhoneMemberEnum){
					return ((PhoneMemberEnum)value).getName();
				}
				return null;
			}
		});
		return config;
	}
	
	/**
	 * 标记已用呼叫线路
	 * @param response
	 */
	@RequestMapping(value="remarkLine")
	public void remarkLine(HttpServletResponse response){
		String callPersonId = getString("callPersonId","");
		if(StringUtils.isEmpty(callPersonId)){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "未获取到线路唯一标识");
		}else{
			try{
				PhoneMember cs = phoneMemberService.getEntityById(callPersonId);
				cs.setOnlineTime(new Date());
				cs.setCurrentUser(getCurrentUser());
				cs.setState(PhoneStateEnum.BUSY);
				phoneMemberService.updateEntity(cs);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "登录成功");
			}catch(Exception e){
				logger.debug(e.getStackTrace());
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "出现异常，请联系管理员。"+e.getMessage());
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 下线
	 * @param response
	 */
	@RequestMapping(value="outLine")
	public void outLine(HttpServletResponse response){
		String callPersonId = getString("callPersonId","");
		if(StringUtils.isEmpty(callPersonId)){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "未获取到线路唯一标识");
		}else{
			try{
				PhoneMember cs = phoneMemberService.getEntityById(callPersonId);
				cs.setCurrentUser(null);
				cs.setOnlineTime(null);
				cs.setState(PhoneStateEnum.FREE);
				phoneMemberService.updateEntity(cs);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "登录成功");
			}catch(Exception e){
				logger.debug(e.getStackTrace());
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "出现异常，请联系管理员。"+e.getMessage());
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 获取渠道商固话页面
	 * @return
	 */
	@RequestMapping(value="numberList")
	public String numberList(ModelMap modelMap){
		modelMap.put("orgInterfaceId", getString("orgInterfaceId",""));
		return "cmct/phone/phoneNumberList" ;
	}
	
	/**
	 * 获取渠道商固话列表
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="getNumberList")
	public void getNumberList(Pagination<PhoneNumber> pagination,HttpServletResponse response){
		try{
			int currentPage = pagination.getCurrentPage();
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("pageNo", currentPage);
			param.put("orgInterfaceId", getString("orgInterfaceId",""));
			Map<String,Object> result = PhoneMemberUtil.getNumberList(param);
			if("SUCCESS".equals(result.get("STATE").toString())){
				JSONArray numberList =  JSONArray.fromObject(result.get("numberList"));
				Integer recordTotal = (Integer) result.get("recordTotal");
				List<PhoneNumber> queryList = new ArrayList<PhoneNumber>();
				for(int i = 0 ; i < numberList.size() ; i ++){
					PhoneNumber cn = new PhoneNumber();
					JSONObject jsonObj = numberList.getJSONObject(i);
					cn.setPhoneNo(jsonObj.getString("phoneNo"));
					cn.setPhoneType(jsonObj.getString("phoneType"));
					cn.setCurrentPackName(jsonObj.getString("currentPackName"));
					cn.setOrgName(jsonObj.getString("orgName"));
					cn.setMobileNo(jsonObj.getString("mobileNo"));
					cn.setStatus(jsonObj.getString("status") == null ? 2 : Integer.valueOf(jsonObj.getString("status")));
					cn.setBuyTime(StringUtils.isEmpty(jsonObj.getString("buyTime"))? null : DateUtil.convertStrToDate(jsonObj.getString("buyTime"),DateUtil.GENERAL_FORMHMS));
					cn.setStopTime(StringUtils.isEmpty(jsonObj.getString("stopTime")) ? null : DateUtil.convertStrToDate(jsonObj.getString("stopTime"),DateUtil.GENERAL_FORMHMS));
					queryList.add(cn);
				}
				pagination.setItems(queryList);
				pagination.setRecordCount(recordTotal == null ? 0 : recordTotal.intValue());
			}
		}catch(Exception ex){
			ex.printStackTrace();
			pagination = new Pagination<PhoneNumber>();
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
}
