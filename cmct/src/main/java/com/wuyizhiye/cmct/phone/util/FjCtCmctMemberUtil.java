package com.wuyizhiye.cmct.phone.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.param.model.ParamLines;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.LoginHolder;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.constant.FjCtCmctConstant;
import com.wuyizhiye.cmct.phone.enums.DialResultEnum;
import com.wuyizhiye.cmct.phone.enums.DialTypeEnum;
import com.wuyizhiye.cmct.phone.model.PhoneCode;
import com.wuyizhiye.cmct.phone.model.PhoneDJCostMember;
import com.wuyizhiye.cmct.phone.model.PhoneDialRecord;
import com.wuyizhiye.cmct.phone.model.PhoneMarket;
import com.wuyizhiye.cmct.phone.model.PhoneMember;

/**
 * @ClassName FjCtCmctMemberUtil
 * @Description 福建电信话务成员工具类
 * @author li.biao
 * @date 2015-5-26
 */
public class FjCtCmctMemberUtil {
	
	public static final String TIP_KEY_STATE = "STATE" ;
	public static final String TIP_KEY_MSG = "MSG" ;
	public static final String TIP_KEY_SUCC = "SUCCESS" ;
	public static final String TIP_KEY_FAIL= "FAIL" ;
	public static final String TIP_VALUE_EXCEPTION = "EXCEPTION" ;
	public static final String SESSIONID = "sessionId" ;
	
	public static final String DXHTTPURL = "dxHttpUrl" ;//存取电信的环境链接
	public static final String DEFAULT_DXHTTPURL = "http://110.84.128.35:3030/httpIntf/dealIntf" ;//默认为正式环境的链接
	
	/**
	 * 系统参数,51置业的客服接听号码 
	 */
	public static final String CMCT_WYZY_PHONENUM = "CMCT_WYZY_PHONENUM" ;
	
	private static QueryExecutor queryExecutor  = (QueryExecutor)ApplicationContextAware.getApplicationContext().getBean("queryExecutor");;
	
	public static String getDxHttpUrl(){
		if(null==LoginHolder.getCurrBaseConfig().get(DXHTTPURL)){
			LoginHolder.getCurrBaseConfig().put(DXHTTPURL, DEFAULT_DXHTTPURL);
		}
		return LoginHolder.getCurrBaseConfig().get(DXHTTPURL);
	}
	
	/**
	 * 拨打电话
	 * @param ppm
	 * @return 成功则取respMap，里面Sessionid为当前成功通话的一次标识符
	 */
	public static Map<String,Object> dailPhone(PhoneMember ppm){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			String reqParam = FjCtCmctTool.buildRequestParam(ppm, "dial",null);
			String requestData = HttpClientUtil.callHttpUrlGet(ppm.getHttpUrl() + "?postData="+reqParam);
			if(!StringUtils.isEmpty(requestData)){
				Map<String,Object> respMap = FjCtCmctTool.parseXmlStr(requestData);
				Object resultObj = respMap.get("Result");
				if(resultObj != null ){
					int result = Integer.valueOf(resultObj.toString());
					if(result == 0){
						msgRst.put("respMap", respMap);
						msgRst.put(TIP_KEY_STATE, TIP_KEY_SUCC);
					}else{
						msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
						msgRst.put(TIP_KEY_MSG, respMap.get("ResultDesc"));
					}
				}else{
					msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
					msgRst.put(TIP_KEY_MSG, "未接收到正确参数");
				}
			}else{
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				msgRst.put(TIP_KEY_MSG, "未请求到任何数据");
			}
		}catch (Exception e) {
			e.printStackTrace();
			msgRst.put(TIP_KEY_STATE, TIP_VALUE_EXCEPTION);
			msgRst.put(TIP_KEY_MSG, e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 挂断电话
	 * @param ppm
	 * @return 
	 */
	public static Map<String,Object> hookPhone(PhoneMember ppm){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			String reqParam = FjCtCmctTool.buildRequestParam(ppm, "dialStop",null);
			String requestData = HttpClientUtil.callHttpUrlGet(ppm.getHttpUrl() + "?postData="+reqParam);
			if(!StringUtils.isEmpty(requestData)){
				Map<String,Object> respMap = FjCtCmctTool.parseXmlStr(requestData);
				Object resultObj = respMap.get("Result");
				if(resultObj != null ){
					int result = Integer.valueOf(resultObj.toString());
					if(result == 0){
						msgRst.put("respMap", respMap);
						msgRst.put(TIP_KEY_STATE, TIP_KEY_SUCC);
					}else{
						msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
						msgRst.put(TIP_KEY_MSG, respMap.get("ResultDesc"));
					}
				}else{
					msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
					msgRst.put(TIP_KEY_MSG, "未接收到正确参数");
				}
			}else{
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				msgRst.put(TIP_KEY_MSG, "未请求到任何数据");
			}
		}catch (Exception e) {
			e.printStackTrace();
			msgRst.put(TIP_KEY_STATE, TIP_VALUE_EXCEPTION);
			msgRst.put(TIP_KEY_MSG, e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 获取号码归属地
	 * @param ppm
	 * @return 
	 */
	public static Map<String,Object> hcodeSearch(String phone){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			String reqParam = FjCtCmctTool.buildRequestParam(null, "hcodeSearch",phone);
			String requestData = HttpClientUtil.callHttpUrlGet(FjCtCmctConstant.BASE_URL + "?postData="+reqParam);
			if(!StringUtils.isEmpty(requestData)){
				Map<String,Object> respMap = FjCtCmctTool.parseXmlStr(requestData);
				Object resultObj = respMap.get("Result");
				if(resultObj != null ){
					int result = Integer.valueOf(resultObj.toString());
					if(result == 0){
						msgRst.put("respMap", respMap);
						msgRst.put(TIP_KEY_STATE, TIP_KEY_SUCC);
					}else{
						msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
						msgRst.put(TIP_KEY_MSG, respMap.get("ResultDesc"));
					}
				}else{
					msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
					msgRst.put(TIP_KEY_MSG, "未接收到正确参数");
				}
			}else{
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				msgRst.put(TIP_KEY_MSG, "未请求到任何数据");
			}
		}catch (Exception e) {
			e.printStackTrace();
			msgRst.put(TIP_KEY_STATE, TIP_VALUE_EXCEPTION);
			msgRst.put(TIP_KEY_MSG, e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * @param phone
	 * @param local 不掉接口,查看本地的归属地
	 * @return
	 */
	public static Map<String,Object> hcodeSearch(String phone,String local){
		if(StringUtils.isEmpty(local)){
			return hcodeSearch(phone);
		}
		Map<String, Object>msgRst=new HashMap<String, Object>();
		try{
			String phonrHCode=phone.substring(0,7);
			Map<String,Object> respMap = new HashMap<String, Object>();
			respMap.put("hCode", phonrHCode);
			List<PhoneCode>codes=queryExecutor.execQuery(PhoneCode.MAPPER+".select", respMap, PhoneCode.class);
			if(null!=codes && codes.size()>0){
				PhoneCode code=codes.get(0);
				respMap.put("City", code.getCity());
				respMap.put("Province", code.getProvince());
				respMap.put("Corp", code.getCorp());
				msgRst.put(TIP_KEY_STATE, TIP_KEY_SUCC);
				msgRst.put("respMap", respMap);
			}else{
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				msgRst.put(TIP_KEY_MSG, "未请求到任何数据");
			}
		}catch(Exception e){
			e.printStackTrace();
			msgRst=hcodeSearch(phone);
		}
		return msgRst;
	}
	
	/**
	 * 查询计费id详单
	 */
	public static Map<String, Object> imsBill(PhoneMember pm) {
		Map<String, Object> msgRst = new HashMap<String, Object>();
		try {
			String reqParam = FjCtCmctTool.buildRequestParam(pm, "imsBill", null);
			reqParam = reqParam.replaceAll(" ", "%20");
			String requestData = HttpClientUtil.callHttpUrlGet(pm.getHttpUrl() + "?postData=" + reqParam);
			if (!StringUtils.isEmpty(requestData)) {
				Map<String, Object> respMap = FjCtCmctTool.parseXmlData(requestData);
				Object resultObj = respMap.get("Result");
				if (resultObj != null) {
					int result = Integer.valueOf(resultObj.toString());
					if (result == 0) {
						msgRst.put("respMap", respMap);
						msgRst.put(TIP_KEY_STATE, TIP_KEY_SUCC);
					} else {
						msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
						msgRst.put(TIP_KEY_MSG, respMap.get("ResultDesc"));
					}
				} else {
					msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
					msgRst.put(TIP_KEY_MSG, "未接收到正确参数");
				}
			} else {
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				msgRst.put(TIP_KEY_MSG, "未请求到任何数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgRst.put(TIP_KEY_STATE, TIP_VALUE_EXCEPTION);
			msgRst.put(TIP_KEY_MSG, e.getMessage());
		}
		return msgRst;

	}
	
	/**
	 * 查询计费id余额
	 */
	public static Map<String, Object> ImsAccount(PhoneMember pm) {
		Map<String, Object> msgRst = new HashMap<String, Object>();
		try {
			String reqParam = FjCtCmctTool.buildRequestParam(pm, "ImsAccount", null);
			reqParam = reqParam.replaceAll(" ", "%20");
			String requestData = HttpClientUtil.callHttpUrlGet(pm.getHttpUrl() + "?postData=" + reqParam);
			if (!StringUtils.isEmpty(requestData)) {
				Map<String, Object> respMap = FjCtCmctTool.parseXmlStr(requestData);
				Object resultObj = respMap.get("Result");
				if (resultObj != null) {
					int result = Integer.valueOf(resultObj.toString());
					if (result == 0) {
						msgRst.put("respMap", respMap);
						msgRst.put(TIP_KEY_STATE, TIP_KEY_SUCC);
					} else {
						msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
						msgRst.put(TIP_KEY_MSG, respMap.get("ResultDesc"));
					}
				} else {
					msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
					msgRst.put(TIP_KEY_MSG, "未接收到正确参数");
				}
			} else {
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				msgRst.put(TIP_KEY_MSG, "未请求到任何数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgRst.put(TIP_KEY_STATE, TIP_VALUE_EXCEPTION);
			msgRst.put(TIP_KEY_MSG, e.getMessage());
		}
		return msgRst;

	}
	
	/**
	 * 通用方法
	 * @param ppm
	 * type 调用类型
	 * @return 
	 */
	public static Map<String,Object>commonMethond(PhoneMember pm,String type){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			String reqParam = FjCtCmctTool.buildRequestParam(pm, type,null);
			reqParam = reqParam.replaceAll(" ", "%20");
			String requestData = HttpClientUtil.callHttpUrlGet(pm.getHttpUrl()+ "?postData="+reqParam);
			if(!StringUtils.isEmpty(requestData)){
				Map<String,Object> respMap = FjCtCmctTool.parseXmlStr(requestData);
				Object resultObj = respMap.get("Result");
				if(resultObj != null ){
					int result = Integer.valueOf(resultObj.toString());
					if(result == 0){
						msgRst.put("respMap", respMap);
						msgRst.put(TIP_KEY_STATE, TIP_KEY_SUCC);
					}else{
						msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
						msgRst.put(TIP_KEY_MSG, respMap.get("ResultDesc"));
					}
				}else{
					msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
					msgRst.put(TIP_KEY_MSG, "未接收到正确参数");
				}
			}else{
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				msgRst.put(TIP_KEY_MSG, "未请求到任何数据");
			}
		}catch (Exception e) {
			e.printStackTrace();
			msgRst.put(TIP_KEY_STATE, TIP_VALUE_EXCEPTION);
			msgRst.put(TIP_KEY_MSG, e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 通用方法
	 * @param ppm
	 * type 调用类型
	 * @return 
	 */
	public static Map<String,Object>commonMarketMethond(PhoneMarket pm,String type){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			String reqParam = FjCtCmctTool.buildMarketRequestParam(pm, type);
			reqParam = reqParam.replaceAll(" ", "%20");
			String requestData = HttpClientUtil.callHttpUrlGet(pm.getPhoneMbp().getHttpUrl()+ "?postData="+reqParam);
			if(!StringUtils.isEmpty(requestData)){
				Map<String,Object> respMap = FjCtCmctTool.parseXmlStr(requestData);
				Object resultObj = respMap.get("Result");
				if(resultObj != null ){
					int result = Integer.valueOf(resultObj.toString());
					if(result == 0){
						msgRst.put("respMap", respMap);
						msgRst.put(TIP_KEY_STATE, TIP_KEY_SUCC);
					}else{
						msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
						msgRst.put(TIP_KEY_MSG, respMap.get("ResultDesc"));
					}
				}else{
					msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
					msgRst.put(TIP_KEY_MSG, "未接收到正确参数");
				}
			}else{
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				msgRst.put(TIP_KEY_MSG, "未请求到任何数据");
			}
		}catch (Exception e) {
			e.printStackTrace();
			msgRst.put(TIP_KEY_STATE, TIP_VALUE_EXCEPTION);
			msgRst.put(TIP_KEY_MSG, e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 手机版拨打电话
	 * @param ppm
	 * @return 成功则取respMap，里面Sessionid为当前成功通话的一次标识符
	 * telType :mobile或则wyzy 为mobile,则phoneNumber为被叫号码,主叫号码为当前登录人的号码,如果为wyzy,phoneNumber为主叫号码,被叫号码通过系统参数设置
	 * 
	 */
	public static Map<String,Object> dailMobilePhone(String phoneNumber,String telType,String personName){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			String callerNbr="";//主叫号码
			String calleeNbr="";//被叫号码
			PhoneDJCostMember member=PhoneMobileUtil.getDingJianCostMember();
			if(member==null){
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				msgRst.put(TIP_KEY_MSG, "未获取到计费号码");
				return msgRst;
			}
			if("wyzy".equals(telType)){//51置业
				callerNbr=phoneNumber;
				calleeNbr=ParamUtils.getParamValueByNumber(FjCtCmctMemberUtil.CMCT_WYZY_PHONENUM);
				if(StringUtils.isEmpty(calleeNbr)){
					msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
					msgRst.put(TIP_KEY_MSG, "系统未设置接听号码..");
					return msgRst;
				}
			}else{//手机版呼出.判断主叫号码的状态
				Map<String, Object>res=PhoneMobileUtil.judgeMobileMemberStatus();
				if(TIP_KEY_FAIL.equals(res.get(TIP_KEY_STATE).toString())){
					return res;
				}
				callerNbr=SystemUtil.getCurrentUser().getPhone();
				calleeNbr=phoneNumber;
			}
			
			String dialUrl=buildDialPhone(member, callerNbr, calleeNbr);
			String requestData = HttpClientUtil.callHttpUrlGet(member.getHttpUrl() + "?postData="+dialUrl);
			if(!StringUtils.isEmpty(requestData)){
				Map<String,Object> respMap = FjCtCmctTool.parseXmlStr(requestData);
				Object resultObj = respMap.get("Result");
				if(resultObj != null ){
					int result = Integer.valueOf(resultObj.toString());
					if(result == 0){
						msgRst.put(SESSIONID, respMap.get("Sessionid"));
						writeRecordDataBase(callerNbr,calleeNbr,personName,telType,respMap.get("Sessionid").toString(),"");
						msgRst.put(TIP_KEY_MSG, "拨打成功");
						msgRst.put(TIP_KEY_STATE, TIP_KEY_SUCC);
					}else{
						msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
						msgRst.put(TIP_KEY_MSG, respMap.get("ResultDesc"));
					}
				}else{
					msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
					msgRst.put(TIP_KEY_MSG, "未接收到正确参数");
				}
			}else{
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				msgRst.put(TIP_KEY_MSG, "未请求到任何数据");
			}
		}catch (Exception e) {
			e.printStackTrace();
			msgRst.put(TIP_KEY_STATE, TIP_VALUE_EXCEPTION);
			msgRst.put(TIP_KEY_MSG, e.getMessage());
		}
		return msgRst ;
	}
	
	private static String buildDialPhone(PhoneDJCostMember member,String callerNbr,String calleeNbr){
		StringBuilder url =  new StringBuilder("");
		String currTimestamp = FjCtCmctTool.getTimestampNow() ;
		url.append("<Request>");
			url.append("<Head>");
				url.append("<MethodName>").append(FjCtCmctConstant.Method.Dial).append("</MethodName>");
				url.append("<Spid>").append(member.getSpid()).append("</Spid>");
				url.append("<Appid>").append(member.getAppid()).append("</Appid>");
				url.append("<Passwd>").append(FjCtCmctTool.sha1Encrypt(member.getPassWd())).append("</Passwd>");
				url.append("<Timestamp>").append(currTimestamp).append("</Timestamp>");
				url.append("<Authenticator>").append(FjCtCmctTool.sha1Encrypt(currTimestamp+FjCtCmctConstant.Method.Dial+member.getSpid()+member.getPassWd())).append("</Authenticator>");
			url.append("</Head>");
			url.append("<Body>");
				url.append("<ChargeNbr>").append(member.getUserId()).append("</ChargeNbr>");
				url.append("<Key>").append(member.getUserKey()).append("</Key>");
				url.append("<DisplayNbr>").append(member.getUserId()).append("</DisplayNbr>");
				url.append("<CallerNbr>").append(callerNbr).append("</CallerNbr>");
				url.append("<CalleeNbr>").append(calleeNbr).append("</CalleeNbr>");
				url.append("<AnswerOnMedia>").append("1").append("</AnswerOnMedia>");
				url.append("<DisplayNbrMode>").append("1").append("</DisplayNbrMode>");
			url.append("</Body>");
		url.append("</Request>");
		return url.toString();
	}
	
	public static void writeRecordDataBase(String callerNbr,String calleeNbr,String personName,String telType,String sessionId,String personId){
		PhoneDialRecord cr = new PhoneDialRecord();
		cr.setUUID();
		String remark="";
		Person person=SystemUtil.getCurrentUser();
		if(null==person){
			Map<String, Object>param=new HashMap<String, Object>();
			param.put("id", StringUtils.isEmpty(personId)?"-1":personId);
			person=queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.select", param, Person.class);
		}
		if("mobile".equals(telType)){
			cr.setCallId(null!=person?person.getId():"");
			cr.setCallName(null!=person?person.getName():"");
			cr.setCurrShowPhone(null!=person?person.getPhone():"");
			cr.setOrg(null!=person?person.getOrg():null);
			cr.setFromPhone(null!=person?person.getPhone():"");
			remark="@手机客户端呼出";
		}else if("wyzy".equals(telType)){
			cr.setCurrShowPhone(callerNbr);
			cr.setFromPhone(callerNbr);
			remark="@51置业客户呼进";
		}else{
			cr.setCurrShowPhone(callerNbr);
			cr.setFromPhone(callerNbr);
		}
		cr.setCallType(DialTypeEnum.WORK);
		cr.setCallTime(new Date());
		cr.setToPhone(calleeNbr);
		cr.setToName(personName+remark);
		cr.setCallBillId(sessionId);//电信铁通共存一个通话id
		cr.setCallResult(DialResultEnum.C_127);
		cr.setCostTime(-2);//-2代表手机客户端  -1代表pc端时间不确定
		cr.setSuffix(DialRecordUtil.getSuffix());
		try{			
			queryExecutor.executeInsert(PhoneDialRecord.MAPPER+".insert", cr);
		}catch (Exception e) {
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("paramnumber", "DataBaseType");
			ParamLines paramLines = queryExecutor.execOneEntity("com.wuyizhiye.basedata.param.dao.ParamLinesDao.selectByTypeNumber", param, ParamLines.class);
			String dataBaseType = paramLines.getParamValue();
			if(dataBaseType.equals("ORACLE")){
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createTableOfOracle", cr);
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createIndexCallIdOfOracle", cr);
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createIndexOrgIdOfOracle", cr);
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createIndexCallTimeOfOracle", cr);
			}else{
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createTableOfMysql", cr);
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createIndexCallIdOfMySql", cr);
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createIndexOrgIdOfMySql", cr);
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createIndexCallTimeOfMySql", cr);
			}
			queryExecutor.executeInsert(PhoneDialRecord.MAPPER+".insert", cr);
		}
	}
	
	public static Map<String,Object> dialPhoneByMobile(String personId,String calleeNbr,String calleeName){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			String callerNbr="";//主叫号码
			PhoneDJCostMember member=PhoneMobileUtil.getDingJianCostMember();
			if(member==null){
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				msgRst.put(TIP_KEY_MSG, "未获取到计费号码");
				return msgRst;
			}
			Map<String, Object>res=PhoneMobileUtil.judgeMobileMemberStatus(personId);
			if(TIP_KEY_FAIL.equals(res.get(TIP_KEY_STATE).toString())){
				return res;
			}
			msgRst.put("callerNbr", res.get("phone"));
			callerNbr = res.get("phone").toString();
			String dialUrl=buildDialPhone(member, callerNbr, calleeNbr);
			String requestData = HttpClientUtil.callHttpUrlGet(member.getHttpUrl() + "?postData="+dialUrl);
			if(!StringUtils.isEmpty(requestData)){
				Map<String,Object> respMap = FjCtCmctTool.parseXmlStr(requestData);
				Object resultObj = respMap.get("Result");
				if(resultObj != null ){
					int result = Integer.valueOf(resultObj.toString());
					if(result == 0){
						msgRst.put(SESSIONID, respMap.get("Sessionid"));
						writeRecordDataBase(callerNbr,calleeNbr,calleeName,"mobile",respMap.get("Sessionid").toString(),personId);
						msgRst.put(TIP_KEY_MSG, "拨打成功");
						msgRst.put(TIP_KEY_STATE, TIP_KEY_SUCC);
					}else{
						msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
						msgRst.put(TIP_KEY_MSG, respMap.get("ResultDesc"));
					}
				}else{
					msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
					msgRst.put(TIP_KEY_MSG, "未接收到正确参数");
				}
			}else{
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				msgRst.put(TIP_KEY_MSG, "未请求到任何数据");
			}
		}catch (Exception e) {
			e.printStackTrace();
			msgRst.put(TIP_KEY_STATE, TIP_VALUE_EXCEPTION);
			msgRst.put(TIP_KEY_MSG, e.getMessage());
		}
		return msgRst;
	}
}
