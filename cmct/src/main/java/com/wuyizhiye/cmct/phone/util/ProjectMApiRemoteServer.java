package com.wuyizhiye.cmct.phone.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.BaseConfigUtil;

/**
 * @ClassName ProjectMApiRemoteServer
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class ProjectMApiRemoteServer {

private static Log log = LogFactory.getLog(ProjectMApiRemoteServer.class);
	
	private static String getBaseServerPath(String methodName,Map<String,String> param){
		StringBuilder sbParam = new StringBuilder("");
		for(String key : param.keySet()){
			sbParam.append("&").append(key).append("=").append(param.get(key));
		}
		return getRemoteServerUrl()+"/wuyiyun/api/projectm/"+methodName+"?dataCenter=dataSource_wuyiyun" + sbParam.toString() ;
//		return "http://localhost:8080/web/api/projectm/"+methodName+"?dataCenter=dataSource_Develope" + sbParam.toString() ; 
	}
	
	private static String getRemoteServerUrl(){
		String remoteServerUrl=BaseConfigUtil.getCurrRemoteHttpUrl();
		if(StringUtils.isEmpty(remoteServerUrl)){
			remoteServerUrl="http://120.25.236.193:9980";
		}
		return remoteServerUrl;
	}
	
	private static String getAllQueryDetailServerPath(Map<String, String> Param){
		return getBaseServerPath("getQueryDatail",Param);
	}
	private static String getPhoneListServerPath(Map<String,String> param){
		return getBaseServerPath("getOrgPhoneList",param);
	}
	
	private static String getAllPhoneListServerPath(Map<String,String> param){
		return getBaseServerPath("getOrgAllPhoneList",param);
	}
	
	private static String getAllPhoneList2ServerPath(Map<String,String> param){
		return getBaseServerPath("getOrgAllPhoneList2",param);
	}
	
	private static String getUpdateOrgPhoneUsePath(Map<String,String> param){
		return getBaseServerPath("updatePhoneUse",param);
	}
	private static String getUpdateOrgPhoneUnUsePath(Map<String,String> param){
		return getBaseServerPath("updatePhoneUnUse",param);
	}
	private static String getHfyeListServerPath(Map<String, String> param){
		return getBaseServerPath("getHfyeList", param);
	}
	private static String getAllPhoneCmrServerPath(Map<String,String> param){
		return getBaseServerPath("getOrgAllPhoneCmr",param);
	}
	private static String getAllPhoneCmdServerPath(Map<String,String> param){
		return getBaseServerPath("getOrgAllPhoneCmd",param);
	}
	private static String getCustomerByOrgIdServerPath(Map<String,String> param){
		return getBaseServerPath("getCustomerByOrgId",param);
	}
	
	private static String getPhoneListByUnMatchServerPath(Map<String,String> param){
		return getBaseServerPath("getPhoneListByUnMatch",param);
	}
	private static String getVoiceByCustomerIdServerPath(Map<String,String> param){
		return getBaseServerPath("getVoiceByCustomerId",param);
	}
	private static String getDeputyNumBillServerPath(Map<String,String> param){
		return getBaseServerPath("getDeputyNumBill",param);
	}
	private static String validationMarketServerPath(Map<String,String> param){
		return getBaseServerPath("validationMarket",param);
	}
	private static String getDingJianCostMemberServerPath(Map<String,String> param){
		return getBaseServerPath("getDingJianCostMember",param);
	}
	
	private static String getDingJianDurationNumberServerPath(Map<String,String> param){
		return getBaseServerPath("getDingJianMobileDuration",param);
	}
	
	private static String marketAccountServerPath(Map<String,String> param){
		return getBaseServerPath("marketAccount",param);
	}
	public static <T> List <T> getHfyeList(Map<String, String> param){
		List<T> result=new ArrayList<T>();
		try {
			//获取客户id
			String requestData=HttpClientUtil.callHttpUrl(getHfyeListServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				List<T> configList=(List<T>) JSONArray.toCollection(JSONArray.fromObject(jObj.get("configList")));
				result=configList;
			}else{
				log.error("ProjectMApiRemoteServer.getPhoneList:未请求到任务数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getHfyeListServerPath:"+e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 获取鼎尖yun上的客户电话
	 * @param <T>
	 * @param pagination 
	 * @param start 第几页
	 * @param rows 多少条
	 * @param status 状态
	 * @param customerId 客户唯一标识
	 * @return
	 */
	public static <T> Pagination<T> getPhoneList(Pagination<T> pagination ,Map<String,String> param){
		try{
			String requestData = HttpClientUtil.callHttpUrl(getPhoneListServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				Object flag = jObj.get("FLAG");
				if(flag == null || StringUtils.isEmpty(flag.toString())){
					String recordCount = jObj.getString("recordCount");
					List<T> items = (List<T>) JSONArray.toCollection(JSONArray.fromObject(jObj.get("items")));
					pagination.setItems(items);
					pagination.setRecordCount(StringUtils.isEmpty(recordCount) ? 0 : Integer.valueOf(recordCount));
					pagination.setExceSql(jObj.getString("exceSql"));
					pagination.setExceTime(jObj.getString("exceTime"));
				}
			}else{
				log.error("ProjectMApiRemoteServer.getPhoneList:未请求到任务数据");
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getPhoneList:"+e.getMessage());
		}
		return pagination ;
	}
	
	/**
	 * 获取未分配的线路,返回字符串,不做转变,by lxl 14.7.31 不转换jsonObject对象,直接字符串截取
	 * @param param
	 * @return
	 */
	public static Map<String, String> getPhoneListByUnMatch(Map<String,String> param){
		Map<String, String>result=new HashMap<String, String>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getPhoneListByUnMatchServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				/*JSONObject jObj = JSONObject.fromObject(requestData);
				Object flag = jObj.get("FLAG");
				if(flag == null || StringUtils.isEmpty(flag.toString())){
					String phoneList = jObj.getString("phoneList");
					result.put("STATE", "SUCCESS");
					result.put("phoneList", phoneList);
				}else{
					result.put("STATE", "FAIL");
					result.put("MSG",jObj.getString("MSG"));
				}*/
				
				if(requestData.indexOf("phoneList")>=0){
					String resultData=requestData.substring(requestData.indexOf(":")+1, requestData.length()-1);
					result.put("STATE", "SUCCESS");
					result.put("phoneList", resultData);
				}else{
					result.put("STATE", "FAIL");				
					result.put("MSG","客户不存在");
				}
			}else{
				result.put("STATE", "FAIL");
				result.put("MSG", "未请求到任务数据");
				log.error("ProjectMApiRemoteServer.getAllPhoneList2:未请求到任务数据");
			}
		}catch (Exception e) {
			result.put("STATE", "FAIL");
			result.put("MSG", e.getMessage());
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getAllPhoneList2:"+e.getMessage());
		}
		return result ;
	}
	
	public static <T> List<T> getAllPhoneList(Map<String,String> param){
		 List<T> result = new ArrayList<T>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getAllPhoneListServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				Object flag = jObj.get("FLAG");
				if(flag == null || StringUtils.isEmpty(flag.toString())){
					String phoneList = jObj.getString("phoneList");
					List<T> items = (List<T>) JSONArray.toCollection(JSONArray.fromObject(phoneList));
					result = items ;
				}
			}else{
				log.error("ProjectMApiRemoteServer.getAllPhoneList:未请求到任务数据");
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getAllPhoneList:"+e.getMessage());
		}
		return result ;
	}
	
	/**
	 * 获取当前用户的所有线路,已分配线路获取最新装填.返回字符串,不做转变 by lxl 14.7.24
	 * @param param
	 * @return
	 */
	public static Map<String, String> getAllPhoneList2(Map<String,String> param){
		Map<String, String>result=new HashMap<String, String>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getAllPhoneList2ServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				//转换太慢,特别是大数据的时候,
				//JSONObject jObj = JSONObject.fromObject(requestData);
				//Object flag = jObj.get("FLAG");
				/*if(flag == null || StringUtils.isEmpty(flag.toString())){
					String phoneList = jObj.getString("phoneList");
					result.put("STATE", "SUCCESS");
					result.put("phoneList", phoneList);
				}else{
					result.put("STATE", "FAIL");
					result.put("MSG",jObj.getString("MSG"));
				}*/
				
				if(requestData.indexOf("phoneList")>=0){
					String resultData=requestData.substring(requestData.indexOf(":")+1, requestData.length()-1);
					result.put("STATE", "SUCCESS");
					result.put("phoneList", resultData);
				}else{
					result.put("STATE", "FAIL");				
					result.put("MSG","客户不存在");
				}
			}else{
				result.put("STATE", "FAIL");
				result.put("MSG", "未请求到任务数据");
				log.error("ProjectMApiRemoteServer.getAllPhoneList2:未请求到任务数据");
			}
		}catch (Exception e) {
			result.put("STATE", "FAIL");
			result.put("MSG", e.getMessage());
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getAllPhoneList2:"+e.getMessage());
		}
		return result ;
	}
	
	/**
	 * 获取某个核算渠道的orgId在鼎尖yun平台上面的账单数据
	 * @param <T>
	 * @param param
	 * @return
	 */
	public static Map<String, String> getAllPhoneCmr(Map<String,String> param){
		Map<String, String>result=new HashMap<String, String>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getAllPhoneCmrServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				Object flag = jObj.get("STATE");
				if(flag != null && "SUCCESS".equals(flag.toString())){
					String phoneCmr = jObj.getString("phoneCmr");
					result.put("STATE", "SUCCESS");
					result.put("phoneCmr", phoneCmr);
				}else{
					result.put("STATE", "FAIL");
					result.put("MSG",jObj.getString("MSG"));
				}
			}else{
				result.put("STATE", "FAIL");
				result.put("MSG", "未请求到任务数据");
				log.error("ProjectMApiRemoteServer.getAllPhoneCm:未请求到任务数据");
			}
		}catch (Exception e) {
			result.put("STATE", "FAIL");
			result.put("MSG", e.getMessage());
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getAllPhoneCm:"+e.getMessage());
		}
		return result ;
	}
	public static Map<String, String> getQueryDetail(Map<String,String> param){
		Map<String, String>result=new HashMap<String, String>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getAllQueryDetailServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				Object flag = jObj.get("STATE");
				if(flag != null && "SUCCESS".equals(flag.toString())){
					String phoneDial = jObj.getString("phoneDial");
					result.put("STATE", "SUCCESS");
					result.put("phoneDial", phoneDial);
				}else{
					result.put("STATE", "FAIL");
					result.put("MSG",jObj.getString("MSG"));
				}
			}else{
				result.put("STATE", "FAIL");
				result.put("MSG", "未请求到任务数据");
				log.error("ProjectMApiRemoteServer.getQueryDetail:未请求到任务数据");
			}
		}catch (Exception e) {
			result.put("STATE", "FAIL");
			result.put("MSG", e.getMessage());
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getQueryDetail:"+e.getMessage());
		}
		return result ;
	}
	/**
	 * 获取某个核算渠道的orgId在鼎尖yun平台上面的账单的明细数据
	 * @param <T>
	 * @param param
	 * @return
	 */
	public static Map<String, String> getAllPhoneCmd(Map<String,String> param){
		Map<String, String>result=new HashMap<String, String>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getAllPhoneCmdServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				Object flag = jObj.get("STATE");
				if(flag != null && "SUCCESS".equals(flag.toString())){
					String phoneCmd = jObj.getString("phoneCmd");
					result.put("STATE", "SUCCESS");
					result.put("phoneCmd", phoneCmd);
				}else{
					result.put("STATE", "FAIL");
					result.put("MSG",jObj.getString("MSG"));
				}
			}else{
				result.put("STATE", "FAIL");
				result.put("MSG", "未请求到任务数据");
				log.error("ProjectMApiRemoteServer.getAllPhoneCm:未请求到任务数据");
			}
		}catch (Exception e) {
			result.put("STATE", "FAIL");
			result.put("MSG", e.getMessage());
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getAllPhoneCm:"+e.getMessage());
		}
		return result ;
	}
	
	/**
	 * 查找客户下的核算渠道
	 * @param <T>
	 * @param param
	 * @return
	 */
	public static Map<String, String> getCustomerByOrgId(Map<String,String> param){
		Map<String, String>result=new HashMap<String, String>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getCustomerByOrgIdServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				Object flag = jObj.get("STATE");
				if(flag != null && "SUCCESS".equals(flag.toString())){
					String pcs = jObj.getString("pcs");
					result.put("pcs", pcs);
					result.put("STATE", "SUCCESS");
				}else{
					result.put("STATE", "FAIL");
					result.put("MSG",jObj.getString("MSG"));
				}
			}else{
				result.put("STATE", "FAIL");
				result.put("MSG", "未请求到任务数据");
				log.error("ProjectMApiRemoteServer.getCustomerByOrgId:未请求到任务数据");
			}
		}catch (Exception e) {
			result.put("STATE", "FAIL");
			result.put("MSG", e.getMessage());
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getCustomerByOrgId:"+e.getMessage());
		}
		return result ;
	}
	
	/**
	 * 更新鼎尖yun上面某个电话的状态为使用中
	 * @param param id 电话id
	 * @return FLAG=SUCC 成功；FLAG=FAIL 失败，
	 * MSG 01-参数无效或者为空；02-平台不存在此记录；03-号码状态未可用；99-平台未返回任何信息
	 */
	public static Map<String,Object> updatePhoneUse(Map<String,String> param){
		Map<String,Object> result = new HashMap<String,Object>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getUpdateOrgPhoneUsePath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				Object flag = jObj.get("FLAG");
				Object msg = jObj.get("MSG") ;
				if(flag != null && !StringUtils.isEmpty(flag.toString())){
					result.put("FLAG", flag);
					result.put("MSG", msg);
				}else{
					result.put("FLAG", "FAIL");
					result.put("MSG", "99");
				}
			}else{
				log.error("ProjectMApiRemoteServer.updatePhoneStatus:未请求到任务数据");
				result.put("FLAG", "FAIL");
				result.put("MSG", "未请求到任务数据");
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.updatePhoneStatus:"+e.getMessage());
			result.put("FLAG", "FAIL");
			result.put("MSG", "网络异常");
		}
		return result ;
	}
	
	/**
	 * 更新鼎尖yun上面某个电话的状态为未使用
	 * @param param id 电话id
	 * @return FLAG=SUCC 成功；FLAG=FAIL 失败，
	 * MSG 01-参数无效或者为空；02-平台不存在此记录；03-号码状态未可用；99-平台未返回任何信息
	 */
	public static Map<String,Object> updatePhoneUnUse(Map<String,String> param){
		Map<String,Object> result = new HashMap<String,Object>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getUpdateOrgPhoneUnUsePath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				Object flag = jObj.get("FLAG");
				Object msg = jObj.get("MSG") ;
				if(flag != null && !StringUtils.isEmpty(flag.toString())){
					result.put("FLAG", flag);
					result.put("MSG", msg);
				}else{
					result.put("FLAG", "FAIL");
					result.put("MSG", "99");
				}
			}else{
				log.error("ProjectMApiRemoteServer.updatePhoneStatus:未请求到任务数据");
				result.put("FLAG", "FAIL");
				result.put("MSG", "未请求到任务数据");
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.updatePhoneStatus:"+e.getMessage());
			result.put("FLAG", "FAIL");
			result.put("MSG", "网络异常");
		}
		return result ;
	}
	
	/**
	 * 获取鼎尖里面的上传的语音
	 * @param param
	 * @return
	 */
	public static Map<String, String> getVoiceByCustomerId(Map<String,String> param){
		Map<String, String>result=new HashMap<String, String>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getVoiceByCustomerIdServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				if(requestData.indexOf("voiceList")>=0){
					String resultData=requestData.substring(requestData.indexOf(":")+1, requestData.length()-1);
					result.put("STATE", "SUCCESS");
					result.put("voiceList", resultData);
				}else{
					result.put("STATE", "FAIL");				
					result.put("MSG",requestData);
				}
			}else{
				result.put("STATE", "FAIL");
				result.put("MSG", "未请求到任务数据");
				log.error("ProjectMApiRemoteServer.getVoiceByCustomerId:未请求到任务数据");
			}
		}catch (Exception e) {
			result.put("STATE", "FAIL");
			result.put("MSG", e.getMessage());
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getVoiceByCustomerId:"+e.getMessage());
		}
		return result ;
	}
	
	/**
	 * 获取鼎尖平台上的副号账单信息
	 * @param param
	 * @return
	 */
	public static Map<String, Object> getDeputyNumBill(Map<String,String> param){
		Map<String, Object>result=new HashMap<String, Object>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getDeputyNumBillServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				if(requestData.indexOf("billList")>=0){
					result.put("STATE", "SUCCESS");
					result.put("billList", requestData);
				}else{
					result.put("STATE", "FAIL");				
					result.put("MSG",requestData.substring(requestData.indexOf(":")+1, requestData.indexOf(",")));
				}
			}else{
				result.put("STATE", "FAIL");
				result.put("MSG", "未请求到任务数据");
				log.error("ProjectMApiRemoteServer.getDeputyNumBill:未请求到任务数据");
			}
		}catch (Exception e) {
			result.put("STATE", "FAIL");
			result.put("MSG", e.getMessage());
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getDeputyNumBill:"+e.getMessage());
		}
		return result ;
	}
	
	/**
	 * 验证营销号码
	 * @param param
	 * @return
	 */
	public static Map<String, Object> validationMarket(Map<String,String> param){
		Map<String, Object>result=new HashMap<String, Object>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(validationMarketServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				String flag = jObj.getString("FLAG");
				if("SUCESS".equals(flag)){
					result.put("STATE", "SUCCESS");
					result.put("MSG", jObj.getString("MSG"));
				}else{
					result.put("STATE", "FAIL");
					result.put("MSG", jObj.getString("MSG"));
				}
			}else{
				result.put("STATE", "FAIL");
				result.put("MSG", "未请求到任务数据");
				log.error("ProjectMApiRemoteServer.validationMarket:未请求到任务数据");
			}
		}catch (Exception e) {
			result.put("STATE", "FAIL");
			result.put("MSG", e.getMessage());
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.validationMarket:"+e.getMessage());
		}
		return result ;
	}
	
	/**
	 * 营销号码余额查看
	 * @param param
	 * @return
	 */
	public static Map<String, Object> marketAccount(Map<String,String> param){
		Map<String, Object>result=new HashMap<String, Object>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(marketAccountServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				String flag = jObj.getString("FLAG");
				if("SUCESS".equals(flag)){
					result.put("STATE", "SUCCESS");
					result.put("djMemberId", jObj.getString("djMemberId"));
					result.put("account", jObj.getString("account"));
				}else{
					result.put("STATE", "FAIL");
					result.put("MSG", jObj.getString("MSG"));
				}
			}else{
				result.put("STATE", "FAIL");
				result.put("MSG", "未请求到任务数据");
				log.error("ProjectMApiRemoteServer.validationMarket:未请求到任务数据");
			}
		}catch (Exception e) {
			result.put("STATE", "FAIL");
			result.put("MSG", e.getMessage());
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.validationMarket:"+e.getMessage());
		}
		return result ;
	}
	
	/**
	 *获取计费号码
	 * @param param
	 * @return
	 */
	public static Map<String, Object> getDingJianCostMember(Map<String,String> param){
		Map<String, Object>result=new HashMap<String, Object>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getDingJianCostMemberServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				String flag = jObj.getString("FLAG");
				if("SUCESS".equals(flag)){
					result.put("STATE", "SUCCESS");
					result.put("phones", jObj.getString("phones"));
				}else{
					result.put("STATE", "FAIL");
					result.put("MSG", jObj.getString("MSG"));
				}
			}else{
				result.put("STATE", "FAIL");
				result.put("MSG", "未请求到任务数据");
				log.error("ProjectMApiRemoteServer.getDingJianCostMember:未请求到任务数据");
			}
		}catch (Exception e) {
			result.put("STATE", "FAIL");
			result.put("MSG", e.getMessage());
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getDingJianCostMember:"+e.getMessage());
		}
		return result ;
	}
	
	/**
	 *获取通话时长
	 * @param param
	 * @return
	 */
	public static Map<String, Object> getDingJianDurationMember(Map<String,String> param){
		Map<String, Object>result=new HashMap<String, Object>();
		try{
			String requestData = HttpClientUtil.callHttpUrl(getDingJianDurationNumberServerPath(param), "");
			if(!StringUtils.isEmpty(requestData)){
				JSONObject jObj = JSONObject.fromObject(requestData);
				String flag = jObj.getString("FLAG");
				if("SUCESS".equals(flag)){
					result.put("STATE", "SUCCESS");
					result.put("phones", jObj.getString("phones"));
				}else{
					result.put("STATE", "FAIL");
					result.put("MSG", jObj.getString("MSG"));
				}
			}else{
				result.put("STATE", "FAIL");
				result.put("MSG", "未请求到任务数据");
				log.error("ProjectMApiRemoteServer.getDingJianDurationNumber:未请求到任务数据");
			}
		}catch (Exception e) {
			result.put("STATE", "FAIL");
			result.put("MSG", e.getMessage());
			e.printStackTrace();
			log.error("ProjectMApiRemoteServer.getDingJianDurationNumber:"+e.getMessage());
		}
		return result ;
	}
}
