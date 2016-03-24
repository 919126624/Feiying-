package com.wuyizhiye.cmct.phone.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.BaseConfigUtil;

/**
 * @ClassName PhoneMainShowUtil
 * @Description 主显号码工具类
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneMainShowUtil {
	
	public static final String TIP_KEY_STATE = "STATE" ;
	public static final String TIP_KEY_MSG = "MSG" ;
	public static final String TIP_KEY_SUCC = "SUCCESS" ;
	public static final String TIP_KEY_FAIL= "FAIL" ;
	public static final String TIP_VALUE_EXCEPTION = "EXCEPTION" ;
	
	private static String getRemoteServerUrl(){
		String remoteServerUrl=BaseConfigUtil.getCurrRemoteHttpUrl();
		if(StringUtils.isEmpty(remoteServerUrl)){
			remoteServerUrl="http://120.25.236.193:9980";
		}
		return remoteServerUrl+"/wuyiyun";
//		return "http://localhost:8080/web";
	}
	
	//构建注册号码获取验证码url
	public static Map<String, Object> getMainShowCode(String customerNumber,String displayNbr){
		Map<String, Object>resMap=new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_getVerificationCode");
		sb.append("&params=").append("{customerNumber:").append("'"+customerNumber+"',").append("displayNbr:").append("'"+displayNbr+"'}");
		String url=getRemoteServerUrl()+sb.toString();
		url = replaceUrl(url);
		String requestData = HttpClientUtil.callHttpUrlGet(url);
		setResMap(requestData, resMap);
		return resMap;
	}
	
	//注册计费号码用来作为主显号码
	public static Map<String, Object> registDisplayNbr(String customerNumber,String displayNbr,String code,String remark){
		Map<String, Object>resMap=new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_registDisplayNbr");
		sb.append("&params=").append("{customerNumber:").append("'"+customerNumber+"',").append("displayNbr:").append("'"+displayNbr+"',").append("reMark:").append("'"+remark+"',");
		sb.append("code:").append("'"+code+"'}");
		String url=getRemoteServerUrl()+sb.toString();
		url = replaceUrl(url);
		String requestData = HttpClientUtil.callHttpUrlGet(url);
		setResMap(requestData, resMap);
		return resMap;
	}
	
	//查找云上的显示号码
	public static Map<String, Object> getDisplayNbr(String customerNumber, String key,String state,int currPage,int pageSize){
		Map<String, Object>resMap=new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_getDisplayNbr");
//		sb.append("/api?").append("apiNumber=").append("PROJECTM0003");
		sb.append("&params=").append("{customerNumber:").append("'"+customerNumber+"',").append("key:").append("'"+key+"',");
		sb.append("state:").append("'"+state+"',").append("currPage:").append(currPage).append(",pageSize:").append(pageSize).append("}");
		String url=getRemoteServerUrl()+sb.toString();
		url = replaceUrl(url);
		String requestData = HttpClientUtil.callHttpUrlGet(url);
		setResMap(requestData, resMap);
		return resMap;
	}
	
	//分配主显号码
	public static Map<String, Object> matchDisplayNbr(String ids){
		Map<String, Object>resMap=new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_matchDisplayNbr");
//		sb.append("/api?").append("apiNumber=").append("PROJECTM0004");
		sb.append("&params=").append("{ids:").append("'"+ids+"'").append("}");
		String url=getRemoteServerUrl()+sb.toString();
		url = replaceUrl(url);
		String requestData = HttpClientUtil.callHttpUrlGet(url);
		setResMap(requestData, resMap);
		return resMap;
	}
	
	//删除主显号码
	public static Map<String, Object> deleteDisplayNbr(String id){
		Map<String, Object>resMap=new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_deleteDisplayNbr");
		sb.append("&params=").append("{id:").append("'"+id+"'").append("}");
		String url=getRemoteServerUrl()+sb.toString();
		url = replaceUrl(url);
		String requestData = HttpClientUtil.callHttpUrlGet(url);
		setResMap(requestData, resMap);
		return resMap;
	}
	
	private static String replaceUrl(String url){
		url = url.replace("{", "%7B");
		url = url.replace("}", "%7D");
		return url;
	}
	
	private static Map<String, Object> setResMap(String requestData,Map<String, Object>resMap){
		if(!StringUtils.isEmpty(requestData)){
			JSONObject jsonObj=JSONObject.fromObject(requestData);
			if(jsonObj.getInt("resultType")==1){
				JSONObject jsonObject=JSONObject.fromObject(jsonObj.get("resultData"));
				if("SUCCESS".equals(jsonObject.getString("STATE"))){
					Set<String>keySet=jsonObject.keySet();
					for(String key:keySet){
						resMap.put(key, jsonObject.getString(key));
					}
					resMap.put(TIP_KEY_STATE, TIP_KEY_SUCC);
				}else{
					resMap.put(TIP_KEY_STATE, TIP_KEY_FAIL);
					resMap.put(TIP_KEY_MSG, jsonObject.getString(TIP_KEY_MSG));
				}
			}else{
				resMap.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				resMap.put(TIP_KEY_MSG, jsonObj.get("resultMsg"));
			}
		}else{
			resMap.put(TIP_KEY_STATE, TIP_KEY_FAIL);
			resMap.put(TIP_KEY_MSG, "返回数据为空");
		}
		return resMap;
	}
	
	private static Map<String, Object>getResMap(StringBuffer sb,Map<String, Object>resMap){
		String url=getRemoteServerUrl()+sb.toString();
		url = replaceUrl(url);
		String requestData = HttpClientUtil.callHttpUrlGet(url);
		setResMap(requestData, resMap);
		return resMap;
	}
}
