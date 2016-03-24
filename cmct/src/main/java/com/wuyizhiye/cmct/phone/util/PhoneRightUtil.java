package com.wuyizhiye.cmct.phone.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;

import com.wuyizhiye.base.common.HttpsVerifyManager;
import com.wuyizhiye.base.dao.impl.QueryExecutorImpl;
import com.wuyizhiye.base.exceptions.BusinessException;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.cmct.phone.model.PhoneConfig;
import com.wuyizhiye.cmct.phone.model.PhoneRight;

/**
 * @ClassName PhoneRightUtil
 * @Description 话务成员工具类
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneRightUtil {
	
	/**
	 * 系统参数：话伴分配的orgId
	 */
	//7593
	public static final String CALL_ORGID = "CALL_ORGID" ;
	
	/**
	 * 系统参数：话伴分配的orgName
	 */
	//测试组织账号
	public static final String CALL_ORGNAME = "CALL_ORGNAME" ;
	
	/**
	 * 系统参数：话伴分配的orgKey
	 */
	//8989ee590264d1aad920fe2169032c37
	public static final String CALL_ORGKEY = "CALL_ORGKEY" ;
	
	/**
	 * 系统参数：新增话伴成员接口URL
	 */
	//public static final String CALL_URL_ADDMENBER = "http://api.33e9.net/client/addMember.do" ;
	//http://183.62.152.139:8093/cp-website/client/addMember.do
	public static final String CALL_URL_ADDRIGHT = "CALL_URL_ADDRIGHT" ;
	
	/**
	 * 系统参数：修改话伴成员接口URL
	 */
	//public static final String CALL_URL_EDITMENBER = "http://api.33e9.net/client/editMember.do" ;
	//http://183.62.152.139:8093/cp-website/client/editMember.do
	public static final String CALL_URL_EDITRIGHT = "CALL_URL_EDITRIGHT" ;
	
	/**
	 * 系统参数：删除话伴成员接口URL
	 */
	//public static final String CALL_URL_DELETEMENBER = "http://api.33e9.net/client/deleteMember.do" ;
	//http://183.62.152.139:8093/cp-website/client/deleteMember.do
	public static final String CALL_URL_DELETERIGHT = "CALL_URL_DELRIGHT" ;
	
	public static final String CALL_URL_BINDRIGHT = "CALL_URL_BINDRIGHT" ;
	
	public static final String CALL_URL_UNBINDRIGHT = "CALL_URL_UNBINDRIGHT" ;
	
	public static final String CALL_URL_SETDEFAULT = "CALL_URL_SETDEFAULT" ;
	
	public static final String CALL_URL_SETDEFAULTNONE = "CALL_URL_SETDEFAULTNONE" ;
	
	/**
	 * 操作类别：添加
	 */
	private static final String OP_TYPE_ADD = "add" ;
	
	/**
	 * 操作类别：编辑修改
	 */
	private static final String OP_TYPE_EDIT = "edit" ;
	
	/**
	 * 操作类别：删除
	 */
	private static final String OP_TYPE_DELETE = "delete" ;
	
	/**
	 * 操作类别：编辑修改
	 */
	private static final String OP_TYPE_BIND = "bind" ;
	
	/**
	 * 操作类别：删除
	 */
	private static final String OP_TYPE_UNBIND = "unbind" ;
	
	/**
	 * 操作类别：设为默认权限
	 */
	private static final String OP_TYPE_SETDEFAULT = "setDefault" ;
	
	/**
	 * 操作类别：取消设为默认权限
	 */
	private static final String OP_TYPE_SETDEFAULTNONE = "setDefaultNone" ;
	
	/**
	 * 请求路径协议：HTTP
	 */
	private static final String URL_PROTOCOL_HTTP = "HTTP" ;
	
	/**
	 * 请求路径协议：HTTPS
	 */
	private static final String URL_PROTOCOL_HTTPS = "HTTPS" ;
	

	
	/**
	 * 添加单个话伴成员
	 * @param cp
	 * @return 状态标识STATE=SUCCESS（成功）=FAIL（失败），错误信息MSG
	 */
	public static Map<String,Object> addRight(PhoneRight cp){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try {
			//构建请求URL
			String reqUrl = buildRequestUrl(cp, OP_TYPE_ADD);
			
			//调用
			Map<String,Object> result = null ;
			URL url = new URL(reqUrl);
			if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
				result = callUrlAndGetResult(reqUrl);
			}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
				result = callHttpsUrlAndGetResult(reqUrl);
			}else{
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG","所配置的请求协议方式不正确");
			}
			if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
				JSONObject jObject = JSONObject.fromObject(result.get("MSG"));
				String errorKey = jObject.getString("responseKey");
				if("00".equals(errorKey)){
					msgRst.put("STATE", "SUCCESS");
					msgRst.put("callRightsID", jObject.get("callRightsID"));
				}else {
					msgRst.put("STATE", "FAIL");
					msgRst.put("MSG", getMsgByErrorKey(OP_TYPE_ADD, errorKey));
					msgRst.put("errNumber", jObject.get("errNumber"));
				}
			}else{
				msgRst = result ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgRst.put("STATE", "EXCEPTION");
			msgRst.put("MSG",e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 修改话伴成员
	 * @param cp
	 * @return 状态标识STATE=SUCCESS（成功）=FAIL（失败），错误信息MSG
	 */
	public static Map<String,Object> editRight(PhoneRight cp){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			//构建请求URL
			String reqUrl = buildRequestUrl(cp, OP_TYPE_EDIT);
			//调用
			Map<String,Object> result = null ;
			URL url = new URL(reqUrl);
			if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
				result = callUrlAndGetResult(reqUrl);
			}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
				result = callHttpsUrlAndGetResult(reqUrl);
			}else{
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG","所配置的请求协议方式不正确");
			}
			if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
				JSONObject jObject = JSONObject.fromObject(result.get("MSG"));
				String errorKey = jObject.getString("responseKey");
				if("00".equals(errorKey)){
					msgRst.put("STATE", "SUCCESS");		
					msgRst.put("callRightsID", jObject.get("callRightsID"));
				}else {
					msgRst.put("STATE", "FAIL");
					msgRst.put("MSG", getMsgByErrorKey(OP_TYPE_EDIT, errorKey));
					msgRst.put("errNumber", jObject.get("errNumber"));
				}
			}else{
				msgRst = result ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgRst.put("STATE", "EXCEPTION");
			msgRst.put("MSG",e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 删除话伴成员
	 * @param cp
	 * @return 状态标识STATE=SUCCESS（成功）=FAIL（失败），错误信息MSG
	 */
	public static Map<String,Object> deleteRight(PhoneRight cp){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			//构建请求URL
			String reqUrl = buildRequestUrl(cp, OP_TYPE_DELETE);
			//调用
			Map<String,Object> result = null ;
			URL url = new URL(reqUrl);
			if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
				result = callUrlAndGetResult(reqUrl);
			}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
				result = callHttpsUrlAndGetResult(reqUrl);
			}else{
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG","所配置的请求协议方式不正确");
			}
			if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
				JSONObject jObject = JSONObject.fromObject(result.get("MSG"));
				String errorKey = jObject.getString("responseKey");
				if("00".equals(errorKey)){
					msgRst.put("STATE", "SUCCESS");
				}else {
					msgRst.put("STATE", "FAIL");
					msgRst.put("MSG", getMsgByErrorKey(OP_TYPE_DELETE, errorKey));
				}
			}else{
				msgRst = result ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgRst.put("STATE", "EXCEPTION");
			msgRst.put("MSG",e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 绑定
	 * @param cp
	 * @return 状态标识STATE=SUCCESS（成功）=FAIL（失败），错误信息MSG
	 */
	public static Map<String,Object> bindRight(PhoneRight cp){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			//构建请求URL
			String reqUrl = buildRequestUrl(cp, OP_TYPE_BIND);
			//调用
			Map<String,Object> result = null ;
			URL url = new URL(reqUrl);
			if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
				result = callUrlAndGetResult(reqUrl);
			}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
				result = callHttpsUrlAndGetResult(reqUrl);
			}else{
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG","所配置的请求协议方式不正确");
			}
			if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
				JSONObject jObject = JSONObject.fromObject(result.get("MSG"));
				String errorKey = jObject.getString("responseKey");
				if("00".equals(errorKey)){
					msgRst.put("STATE", "SUCCESS");
				}else {
					msgRst.put("STATE", "FAIL");
					msgRst.put("MSG", getMsgByErrorKey(OP_TYPE_BIND, errorKey));
					msgRst.put("errNumber", jObject.get("errNumber"));
				}
			}else{
				msgRst = result ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgRst.put("STATE", "EXCEPTION");
			msgRst.put("MSG",e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 绑定
	 * @param cp
	 * @return 状态标识STATE=SUCCESS（成功）=FAIL（失败），错误信息MSG
	 */
	public static Map<String,Object> unbindRight(PhoneRight cp){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			//构建请求URL
			String reqUrl = buildRequestUrl(cp, OP_TYPE_UNBIND);
			//调用
			Map<String,Object> result = null ;
			URL url = new URL(reqUrl);
			if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
				result = callUrlAndGetResult(reqUrl);
			}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
				result = callHttpsUrlAndGetResult(reqUrl);
			}else{
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG","所配置的请求协议方式不正确");
			}
			if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
				JSONObject jObject = JSONObject.fromObject(result.get("MSG"));
				String errorKey = jObject.getString("responseKey");
				if("00".equals(errorKey)){
					msgRst.put("STATE", "SUCCESS");
				}else {
					msgRst.put("STATE", "FAIL");
					msgRst.put("MSG", getMsgByErrorKey(OP_TYPE_UNBIND, errorKey));
					msgRst.put("errNumber", jObject.get("errNumber"));
				}
			}else{
				msgRst = result ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgRst.put("STATE", "EXCEPTION");
			msgRst.put("MSG",e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 绑定
	 * @param cp
	 * @return 状态标识STATE=SUCCESS（成功）=FAIL（失败），错误信息MSG
	 */
	public static Map<String,Object> bindOrgDefaultRight(PhoneRight cp){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			//构建请求URL
			String reqUrl = buildRequestUrl(cp, OP_TYPE_SETDEFAULT);
			//调用
			Map<String,Object> result = null ;
			URL url = new URL(reqUrl);
			if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
				result = callUrlAndGetResult(reqUrl);
			}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
				result = callHttpsUrlAndGetResult(reqUrl);
			}else{
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG","所配置的请求协议方式不正确");
			}
			if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
				JSONObject jObject = JSONObject.fromObject(result.get("MSG"));
				String errorKey = jObject.getString("responseKey");
				if("00".equals(errorKey)){
					msgRst.put("STATE", "SUCCESS");
				}else {
					msgRst.put("STATE", "FAIL");
					msgRst.put("MSG", getMsgByErrorKey(OP_TYPE_SETDEFAULT, errorKey));
					msgRst.put("errNumber", jObject.get("errNumber"));
				}
			}else{
				msgRst = result ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgRst.put("STATE", "EXCEPTION");
			msgRst.put("MSG",e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 绑定
	 * @param cp
	 * @return 状态标识STATE=SUCCESS（成功）=FAIL（失败），错误信息MSG
	 */
	public static Map<String,Object> unbindOrgDefaultRight(PhoneRight cp){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			//构建请求URL
			String reqUrl = buildRequestUrl(cp, OP_TYPE_SETDEFAULTNONE);
			//调用
			Map<String,Object> result = null ;
			URL url = new URL(reqUrl);
			if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
				result = callUrlAndGetResult(reqUrl);
			}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
				result = callHttpsUrlAndGetResult(reqUrl);
			}else{
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG","所配置的请求协议方式不正确");
			}
			if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
				JSONObject jObject = JSONObject.fromObject(result.get("MSG"));
				String errorKey = jObject.getString("responseKey");
				if("00".equals(errorKey)){
					msgRst.put("STATE", "SUCCESS");
				}else {
					msgRst.put("STATE", "FAIL");
					msgRst.put("MSG", getMsgByErrorKey(OP_TYPE_SETDEFAULTNONE, errorKey));
					msgRst.put("errNumber", jObject.get("errNumber"));
				}
			}else{
				msgRst = result ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgRst.put("STATE", "EXCEPTION");
			msgRst.put("MSG",e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 构建请求URL
	 * @param cp
	 * @param type
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String buildRequestUrl(PhoneRight cp ,String type) throws Exception{
		
		StringBuilder urlBuilder = new StringBuilder();
		
		//读取操作URL；后续要配置到系统参数里面，读取系统参数
		if(OP_TYPE_ADD.equals(type)){
			urlBuilder.append(getPhoneConfigParam(cp.getCallOrgId(),CALL_URL_ADDRIGHT));
			urlBuilder.append("?");
			urlBuilder.append(cp.getAddParaStr());
		}else if(OP_TYPE_EDIT.equals(type)){
			urlBuilder.append(getPhoneConfigParam(cp.getCallOrgId(),CALL_URL_EDITRIGHT));
			urlBuilder.append("?");
			urlBuilder.append(cp.getUpdParaStr());
		}else if(OP_TYPE_DELETE.equals(type)){
			urlBuilder.append(getPhoneConfigParam(cp.getCallOrgId(),CALL_URL_DELETERIGHT));
			urlBuilder.append("?");
			urlBuilder.append(cp.getDelStr());
		}else if(OP_TYPE_BIND.equals(type)){
			urlBuilder.append(getPhoneConfigParam(cp.getCallOrgId(),CALL_URL_BINDRIGHT));
			urlBuilder.append("?");
			urlBuilder.append(cp.getBindStr());
		}else if(OP_TYPE_UNBIND.equals(type)){
			urlBuilder.append(getPhoneConfigParam(cp.getCallOrgId(),CALL_URL_UNBINDRIGHT));
			urlBuilder.append("?");
			urlBuilder.append(cp.getUnBindStr());
		}else if(OP_TYPE_SETDEFAULT.equals(type)){
			urlBuilder.append(getPhoneConfigParam(cp.getCallOrgId(),CALL_URL_SETDEFAULT));
			urlBuilder.append("?");
			urlBuilder.append(cp.getOrgDefaultStr());
		}else if(OP_TYPE_SETDEFAULTNONE.equals(type)){
			urlBuilder.append(getPhoneConfigParam(cp.getCallOrgId(),CALL_URL_SETDEFAULTNONE));
			urlBuilder.append("?");
			urlBuilder.append(cp.getOrgDefaultStr());
		}
		urlBuilder.append("&orgId=").append(getPhoneConfigParam(cp.getCallOrgId(),CALL_ORGID));
		urlBuilder.append("&").append("orgKey=").append(getPhoneConfigParam(cp.getCallOrgId(),CALL_ORGKEY));			
		
		return urlBuilder.toString() ;
	}
	
	/**
	 * 调用URL，并且获取返回参数
	 * 适用于HTTP协议
	 * @param urlString
	 * @return
	 */
	public static Map<String,Object> callUrlAndGetResult(String urlString)  {  
		Map<String,Object> result = new HashMap<String,Object>();
		String msg = "";   
        try {
        	//打开连接
            URL url = new URL(urlString);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setDoOutput(true);  
            conn.setRequestMethod("POST");  
            
            //构建流，获取返回值
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));  
            String line;  
            while ((line = in.readLine()) != null) {  
                msg += line;  
            }  
            in.close();  
            result.put("STATE", "SUCCESS");
            result.put("MSG", msg);
        } catch (Exception e) {  
             e.printStackTrace();
             result.put("STATE", "EXCEPTION");
             result.put("MSG", e.getMessage());
        }  
        return result;  
    }  
	
	/**
	 * 调用URL，并且获取返回参数
	 * 适用于HTTPS协议
	 * @param urlString
	 * @return
	 */
	public static Map<String,Object> callHttpsUrlAndGetResult(String urlString)  { 
		
		class TrustAnyHostnameVerifier implements HostnameVerifier {
			@Override
			public boolean verify(String hostname, SSLSession sslSession) {
				// TODO Auto-generated method stub
				return true;
			}
		}
		
		Map<String,Object> result = new HashMap<String,Object>();
		String msg = "";   
        try {
        	
        	URL url = new URL(urlString);
        	
        	//自定义HTTPS安全认证管理器
        	HttpsVerifyManager hvm = new HttpsVerifyManager();
        	TrustManager tm[] = {hvm} ;
        	//得到上下文
        	SSLContext ctx = SSLContext.getInstance("SSL");
        	//初始化
        	ctx.init(null, tm, null);
        	HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        	conn.setSSLSocketFactory(ctx.getSocketFactory());
        	conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        	conn.setRequestProperty("Charset", "GBK");
        	conn.connect();        	
            
            //构建流，获取返回值
        	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"GBK"));  
        	String line;  
            while ((line = in.readLine()) != null) {  
                msg += line;  
            }  
            in.close();  
            result.put("STATE", "SUCCESS");
            result.put("MSG", msg);
        } catch (Exception e) {  
             e.printStackTrace();
             result.put("STATE", "EXCEPTION");
             result.put("MSG", e.getMessage());
        }  
        return result;  
    }
	
	/**
	 * 跟进操作类别和错误代码返回信息
	 * @param type
	 * @param errorKey
	 * @return
	 */
	public static String getMsgByErrorKey(String type,String errorKey){
		String msg = "" ;
		if(OP_TYPE_ADD.equals(type)){
			if("01".equals(errorKey)){
				msg = "国际长途模板不存在"; 
			}else if("02".equals(errorKey)){
				msg = "超过最大黑名单的个数"; 
			}else if("03".equals(errorKey)){
				msg = "成员已经存在" ;
			}else if("13".equals(errorKey)){
				msg = "组织ID不存在";
			}else if("14".equals(errorKey)){
				msg = "组织Key错误";
			}else if("15".equals(errorKey)){
				msg = "接口系统异常";
			}else if("16".equals(errorKey)){
				msg = "发送信息格式错误" ;
			}else{
				msg = "其他错误" ;
			}
			msg = "addRight:" + msg;
		}else if(OP_TYPE_EDIT.equals(type)){
			if("01".equals(errorKey)){
				msg = "国际长途模板不存在"; 
			}else if("02".equals(errorKey)){
				msg = "超过最大黑名单的个数"; 
			}else if("03".equals(errorKey)){
				msg = "该组织下不存在此呼叫权限ID " ;
			}else if("04".equals(errorKey)){
				msg = "号码不在黑名单列表中" ;
			}else if("05".equals(errorKey)){
				msg = "呼叫权限ID不存在" ;
			}else if("13".equals(errorKey)){
				msg = "组织ID不存在";
			}else if("14".equals(errorKey)){
				msg = "组织Key错误";
			}else if("15".equals(errorKey)){
				msg = "接口系统异常";
			}else if("16".equals(errorKey)){
				msg = "发送信息格式错误" ;
			}else{
				msg = "其他错误" ;
			}
			msg = "editRight:" + msg;
		}else if(OP_TYPE_DELETE.equals(type)){
			if("03".equals(errorKey)){
				msg = "该组织下不存在此呼叫权限ID" ;
			}else if("05".equals(errorKey)){
				msg = "呼叫权限ID不存在" ;
			}else if("13".equals(errorKey)){
				msg = "组织ID不存在";
			}else if("14".equals(errorKey)){
				msg = "组织Key错误";
			}else if("15".equals(errorKey)){
				msg = "接口系统异常";
			}else if("16".equals(errorKey)){
				msg = "发送信息格式错误" ;
			}else{
				msg = "其他错误" ;
			}
			msg = "deleteRight:" + msg;
		}else if(OP_TYPE_BIND.equals(type)){
			if("12".equals(errorKey)){
				msg = "去电号码不是该组织中的号码" ;
			}else if("13".equals(errorKey)){
				msg = "组织ID不存在";
			}else if("14".equals(errorKey)){
				msg = "组织Key错误";
			}else if("15".equals(errorKey)){
				msg = "接口系统异常";
			}else if("16".equals(errorKey)){
				msg = "发送信息格式错误" ;
			}else{
				msg = "其他错误" ;
			}
			msg = "bindRight:" + msg;
		}else if(OP_TYPE_UNBIND.equals(type)){
			if("12".equals(errorKey)){
				msg = "去电号码不是该组织中的号码" ;
			}else if("13".equals(errorKey)){
				msg = "组织ID不存在";
			}else if("14".equals(errorKey)){
				msg = "组织Key错误";
			}else if("15".equals(errorKey)){
				msg = "接口系统异常";
			}else if("16".equals(errorKey)){
				msg = "发送信息格式错误" ;
			}else{
				msg = "其他错误" ;
			}
			msg = "unbindRight:" + msg;
		}else if(OP_TYPE_SETDEFAULT.equals(type)){
			if("12".equals(errorKey)){
				msg = "去电号码不是该组织中的号码" ;
			}else if("13".equals(errorKey)){
				msg = "组织ID不存在";
			}else if("14".equals(errorKey)){
				msg = "组织Key错误";
			}else if("15".equals(errorKey)){
				msg = "接口系统异常";
			}else if("16".equals(errorKey)){
				msg = "发送信息格式错误" ;
			}else{
				msg = "其他错误" ;
			}
			msg = "setDefault:" + msg;
		}else if(OP_TYPE_SETDEFAULTNONE.equals(type)){
			if("12".equals(errorKey)){
				msg = "去电号码不是该组织中的号码" ;
			}else if("13".equals(errorKey)){
				msg = "组织ID不存在";
			}else if("14".equals(errorKey)){
				msg = "组织Key错误";
			}else if("15".equals(errorKey)){
				msg = "接口系统异常";
			}else if("16".equals(errorKey)){
				msg = "发送信息格式错误" ;
			}else{
				msg = "其他错误" ;
			}
			msg = "setDefaultNone:" + msg;
		}
		return msg;
	}
	
	/**
	 * 查找话务参数系统配置
	 * @param orgId 核算渠道
	 * @param code 参数代码
	 * @return
	 * @throws Exception
	 */
	public static String getPhoneConfigParam(String orgId,String code) throws Exception{
		String val = "";
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgId", orgId);
		param.put("isEnable", CommonFlagEnum.YES);
		List<PhoneConfig> configList = QueryExecutorImpl.getInstance().execQuery(PhoneConfig.MAPPER+".select", param, PhoneConfig.class);
		if(configList !=null && configList.size() > 0 ){
			PhoneConfig config = configList.get(0);
			if(CALL_URL_ADDRIGHT.equals(code)){
				val = config.getAddRightUrl();
			}else if(CALL_URL_EDITRIGHT.equals(code)){
				val = config.getUpdRightUrl();
			}else if(CALL_URL_DELETERIGHT.equals(code)){
				val = config.getDelRightUrl();
			}else if(CALL_URL_BINDRIGHT.equals(code)){
				val = config.getBindRightUrl();
			}else if(CALL_URL_SETDEFAULT.equals(code)){
				val = config.getBindRightUrl();
			}else if(CALL_URL_UNBINDRIGHT.equals(code)){
				val = config.getUnbindRightUrl();
			}else if(CALL_URL_SETDEFAULTNONE.equals(code)){
				val = config.getUnbindRightUrl();
			}else if(CALL_ORGID.equals(code)){
				val = config.getOrgId() ;
			}else if(CALL_ORGNAME.equals(code)){
				val = config.getOrgName() ;
			}else if(CALL_ORGKEY.equals(code)){
				val = config.getOrgKey() ;
			}else {
				throw new BusinessException(code+"--所请求的参数代码未找到接口");
			}
		}else{
			throw new BusinessException(code+"--系统参数未设置值或者未启用");
		}
		return val ;
	}
	
	public  static void main(String[] args){
		PhoneRight cp = new PhoneRight();
		
	}
}
