package com.wuyizhiye.cmct.ucs.util;

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

import net.sf.json.JSONArray;

import com.wuyizhiye.base.common.HttpsVerifyManager;
import com.wuyizhiye.base.dao.impl.QueryExecutorImpl;
import com.wuyizhiye.base.exceptions.BusinessException;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneConfig;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneMember;

/**
 * @ClassName UcsPhoneMemberUtil
 * @Description ucs话务工具类
 * @author li.biao
 * @date 2015-5-26
 */
public class UcsPhoneMemberUtil {

	/**
	 * 系统参数：web呼叫URL
	 */
	public static final String UCSCALL_URL = "UCSCALL_URL" ;
	
	/**
	 * 用户登录
	 */
	public static final String UCSLOGIN_URL = "UCSLOGIN_URL" ;
	
	/**
	 *增加坐席 
	 */
	public static final String UCSPHONEADD_URL = "UCSPHONEADD_URL" ;
	
	/**
	 *修改坐席 
	 */
	public static final String UCSPHONEEDIT_URL = "UCSPHONEEDIT_URL" ;
	
	/**
	 *删除坐席 
	 */
	public static final String UCSPHONEDELETE_URL = "UCSPHONEDELETE_URL" ;
	
	/**
	 * 查看坐席
	 */
	public static final String UCSPHONESELECT_URL = "UCSPHONESELECT_URL" ;
	
	/**
	 * 通话记录查看
	 */
	public static final String UCSCALLRECORD_URL = "UCSCALLRECORD_URL" ;
	
	/**
	 * 语音记录查看
	 */
	public static final String UCSCALLRECORDING_URL = "UCSCALLRECORDING_URL" ;
	
	/**
	 * 语音记录下载
	 */
	public static final String UCSCALLRECORDINGDOWNLOAD_URL = "UCSCALLRECORDINGDOWNLOAD_URL" ;
	
	/**
	 * 查看机构ID
	 */
	public static final String UCSAGENTSELECT_URL = "UCSAGENTSELECT_URL" ;
	
	/**
	 * 企业开户
	 */
	public static final String UCSAGENTADD_URL = "UCSAGENTADD_URL" ;
	
	/**
	 *删除企业 
	 */
	public static final String UCSAGENTDELETE_URL = "UCSAGENTDELETE_URL" ;
	
	/**
	 * 修改企业
	 */
	public static final String UCSAGENTUPDATE_URL = "UCSAGENTUPDATE_URL" ;
	
	/**
	 * 挂断呼叫
	 */
	public static final String UCSPHONEHANG_URL = "UCSPHONEHANG_URL" ;
	
	/**
	 * 获取呼叫状态
	 */
	public static final String UCSPHONESTATUS_URL = "UCSPHONESTATUS_URL" ;
	
	/**
	 * 坐席示忙
	 */
	public static final String UCSPHONEBUSY_URL = "UCSPHONEBUSY_URL" ;
	
	/**
	 * 坐席示闲
	 */
	public static final String UCSPHONEFREE_URL = "UCSPHONEFREE_URL" ;
	
	/**
	 * 去电显示号码
	 */
	public static final String UCSSHOWTELNO_URL = "UCSSHOWTELNO_URL" ;
	
	/**
	 * 查看通话状态
	 */
	public static final String UCSCALLSTATUS_URL = "UCSCALLSTATUS_URL" ;
	
	/**
	 * 禁用启用
	 */
	public static final String UCSUSERDISABLE_URL = "UCSUSERDISABLE_URL" ;
	
	/**
	 * 上传去电号码
	 */
	public static final String UCSUPLOADSHOWNO_URL = "UCSUPLOADSHOWNO_URL" ;
	
	/**
	 * key默认字符
	 */
	public static final String UCSPHONE_KEY = "UCSPHONE_KEY" ;
	
	/**
	 * 请求路径协议：HTTP
	 */
	private static final String URL_PROTOCOL_HTTP = "HTTP" ;
	
	/**
	 * 请求路径协议：HTTPS
	 */
	private static final String URL_PROTOCOL_HTTPS = "HTTPS" ;
	
	/**
	 * ucs话务通用通讯方法
	 * @param cp
	 * @return
	 */
	public static Map<String,Object> ucsPhoneUrl(UcsPhoneMember member,String type){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try {
			//构建请求URL
			String reqUrl = buildRequestUrl(type,member );
			
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
				String errorKey = (String) result.get("MSG");
				
				if(errorKey.matches("^-?\\d+$")){
					if(Integer.parseInt(errorKey)>=0){
						msgRst.put("STATE", "SUCCESS");
						msgRst.put("MSG", errorKey);
					}else{
						msgRst.put("STATE", "FAIL");
						msgRst.put("MSG", errorKey);
					}
				}else {
					msgRst.put("STATE", "FAIL");
					//msgRst.put("MSG", getMsgByErrorKey(UCSCALL_URL, errorKey));
					msgRst.put("MSG", errorKey);
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
	 * 获取渠道商固话列表
	 * @param userId 用户号码
	 * @param pageNo 当前页
	 * @return STATE：查询状态；numberList：号码列表； pageNo: 返回结果的当前页 ；pageTotal:总页数
	 */
	public static Map<String,Object> getCallRecordList(UcsPhoneMember member) throws Exception{
		Map<String,Object> msgRst = new HashMap<String,Object>();
		String reqUrl = buildRequestUrl(UCSCALLRECORD_URL,member);
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
			return msgRst ;
		}
		if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
			JSONArray jArray = JSONArray.fromObject(result.get("MSG"));
			if((jArray == null || jArray.size() == 0)){
				msgRst.put("STATE", "SUCCESS");
				msgRst.put("MSG", "没有通话记录");
			}else{
				msgRst.put("callRecordList",  jArray.toString());
				msgRst.put("STATE", "SUCCESS");
			}
		}else{
			msgRst = result ;
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
	public static String buildRequestUrl(String type,UcsPhoneMember member) throws Exception{
		
		StringBuilder urlBuilder = new StringBuilder();		
		if(UCSCALL_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSCALL_URL));
			urlBuilder.append("?").append("userid=").append(member.getTelNo());
			urlBuilder.append("&").append("called=").append(member.getCallNo());
			urlBuilder.append("&").append("enckey=").append(member.getKey());
		}else if(UCSLOGIN_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSLOGIN_URL));
			urlBuilder.append("?").append("userid=").append(member.getUserNo());
			urlBuilder.append("&").append("passwd=").append(member.getPasswd());
			urlBuilder.append("&").append("enckey=").append(member.getKey());
		}else if(UCSPHONEADD_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSPHONEADD_URL));
			urlBuilder.append("?").append("userName=").append(member.getUserName());
			urlBuilder.append("&").append("telNo=").append(member.getTelNo());
			urlBuilder.append("&").append("agentid=").append(member.getUcsPhoneAgent().getAgentId());
			urlBuilder.append("&").append("userType=").append(member.getUserType());
			urlBuilder.append("&").append("passwd=").append(member.getPasswd());
			urlBuilder.append("&").append("userNo=").append(member.getUserNo());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}else if(UCSPHONEEDIT_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSPHONEEDIT_URL));
			urlBuilder.append("?").append("userName=").append(member.getUserName());
			urlBuilder.append("&").append("telNo=").append(member.getTelNo());
			urlBuilder.append("&").append("userType=").append(member.getUserType());
			urlBuilder.append("&").append("passwd=").append(member.getPasswd());
			urlBuilder.append("&").append("userNo=").append(member.getUserNo());
			urlBuilder.append("&").append("usernote=").append(member.getRemark());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}else if(UCSPHONEDELETE_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSPHONEDELETE_URL));
			urlBuilder.append("?").append("telNo=").append(member.getTelNo());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}else if(UCSPHONESELECT_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSPHONESELECT_URL));
			urlBuilder.append("?").append("telNo=").append(member.getTelNo());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}else if(UCSCALLRECORD_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSCALLRECORD_URL));
			urlBuilder.append("?").append("userid=").append(member.getTelNo());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}else if(UCSCALLRECORDING_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSCALLRECORDING_URL));
			urlBuilder.append("?").append("userid=").append(member.getTelNo());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}else if(UCSCALLRECORDINGDOWNLOAD_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSCALLRECORDINGDOWNLOAD_URL));
			urlBuilder.append("?").append("Svmixfullname=").append(member.getSvmixFullName());
		}else if(UCSAGENTSELECT_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSAGENTSELECT_URL));
			urlBuilder.append("?").append("agentName=").append(member.getUcsPhoneAgent().getAgentName());
			urlBuilder.append("&").append("passwd=").append(member.getUcsPhoneAgent().getPasswd());
			urlBuilder.append("&").append("key=").append(member.getUcsPhoneAgent().getKey());
		}else if(UCSAGENTADD_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSAGENTADD_URL));
			urlBuilder.append("?").append("creater=").append(member.getUcsPhoneAgent().getDealerId());
			urlBuilder.append("&").append("agentname=").append(member.getUcsPhoneAgent().getAgentName());
			urlBuilder.append("&").append("passwd=").append(member.getUcsPhoneAgent().getPasswd());
			urlBuilder.append("&").append("agenttype=").append(member.getUcsPhoneAgent().getDealerType());
			urlBuilder.append("&").append("key=").append(member.getUcsPhoneAgent().getKey());
			urlBuilder.append("&").append("telno=").append(member.getUcsPhoneAgent().getTelNo());
		}else if(UCSAGENTDELETE_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSAGENTDELETE_URL));
			urlBuilder.append("?").append("id=").append(member.getUcsPhoneAgent().getAgentId());
			urlBuilder.append("&").append("username=").append(member.getUcsPhoneAgent().getDealerUserName());
			urlBuilder.append("&").append("key=").append(member.getUcsPhoneAgent().getKey());
		}else if(UCSAGENTUPDATE_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSAGENTUPDATE_URL));
			urlBuilder.append("?").append("agentId=").append(member.getUcsPhoneAgent().getAgentId());
			urlBuilder.append("&").append("passwd=").append(member.getUcsPhoneAgent().getPasswd());
			urlBuilder.append("&").append("telNo=").append(member.getUcsPhoneAgent().getTelNo());
			urlBuilder.append("&").append("calledCheck=").append(member.getUcsPhoneAgent().getCalledCheck());
			urlBuilder.append("&").append("clientNumber=").append(member.getUcsPhoneAgent().getClientNumber());
			urlBuilder.append("&").append("key=").append(member.getUcsPhoneAgent().getKey());
		}else if(UCSPHONEHANG_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSPHONEHANG_URL));
			urlBuilder.append("?").append("telNo=").append(member.getTelNo());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}else if(UCSPHONESTATUS_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSPHONESTATUS_URL));
			urlBuilder.append("?").append("agentName=").append(member.getUcsPhoneAgent().getAgentId());
			urlBuilder.append("&").append("passwd=").append(member.getUcsPhoneAgent().getPasswd());
			urlBuilder.append("&").append("key=").append(member.getUcsPhoneAgent().getKey());
		}else if(UCSPHONEBUSY_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSPHONEBUSY_URL));
			urlBuilder.append("?").append("telNo=").append(member.getTelNo());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}else if(UCSPHONEFREE_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSPHONEFREE_URL));
			urlBuilder.append("?").append("telNo=").append(member.getTelNo());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}else if(UCSSHOWTELNO_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSSHOWTELNO_URL));
			urlBuilder.append("?").append("usertel=").append(member.getTelNo());
			urlBuilder.append("&").append("displayno=").append(member.getShowTel().getShowTelNo());
			//urlBuilder.append("&").append("creater=").append(member.getUserName());
			//urlBuilder.append("&").append("passwd=").append(member.getPasswd());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}else if(UCSCALLSTATUS_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSCALLSTATUS_URL));
			urlBuilder.append("?").append("manager=").append(member.getUserName());
			urlBuilder.append("&").append("passwd=").append(member.getPasswd());
			urlBuilder.append("&").append("ip=").append(member.getIp());
			urlBuilder.append("&").append("passwd=").append(member.getPasswd());
			urlBuilder.append("&").append("agent=").append(member.getUcsPhoneAgent().getAgentId());
			urlBuilder.append("&").append("caller=").append(member.getTelNo());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}else if(UCSUSERDISABLE_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSUSERDISABLE_URL));
			urlBuilder.append("?").append("telNo=").append(member.getTelNo());
			urlBuilder.append("&").append("available=").append(member.isUserDisable());
			urlBuilder.append("&").append("agentid=").append(member.getUcsPhoneAgent().getAgentId());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}else if(UCSUPLOADSHOWNO_URL.equals(type)){
			urlBuilder.append(getPhoneConfigParam(UCSUPLOADSHOWNO_URL));
			urlBuilder.append("?").append("agentId=").append(member.getShowTel().getAgentId());
			urlBuilder.append("&").append("shownos=").append(member.getShowTel().getShowTelNoMore());
			urlBuilder.append("&").append("key=").append(member.getKey());
		}		
		return urlBuilder.toString() ;
	}
	/**
	 * 查找话务参数系统配置
	 * @param orgId 核算渠道
	 * @param code 参数代码
	 * @return
	 * @throws Exception
	 */
	public static String getPhoneConfigParam(String code) throws Exception{
		String val = "";
		List<UcsPhoneConfig> configList = QueryExecutorImpl.getInstance().execQuery(UcsPhoneConfig.MAPPER+".select", null, UcsPhoneConfig.class);
		if(configList !=null && configList.size() > 0 ){
			UcsPhoneConfig config = configList.get(0);
			if(UCSCALL_URL.equals(code)){
				val = config.getUcsCallUrl();
			}else if(UCSLOGIN_URL.equals(code)){
				val = config.getUcsLoginUrl();
			}else if(UCSPHONEADD_URL.equals(code)){
				val = config.getUcsPhoneAddURL();
			}else if(UCSPHONEEDIT_URL.equals(code)){
				val = config.getUcsPhoneEditUrl();
			}else if(UCSPHONEDELETE_URL.equals(code)){
				val = config.getUcsPhoneDeleteUrl();
			}else if(UCSPHONESELECT_URL.equals(code)){
				val = config.getUcsPhoneSelectUrl();
			}else if(UCSCALLRECORD_URL.equals(code)){
				val = config.getUcsCallRecordUrl();
			}else if(UCSCALLRECORDING_URL.equals(code)){
				val = config.getUcsCallRecordingUrl();
			}else if(UCSCALLRECORDINGDOWNLOAD_URL.equals(code)){
				val = config.getUcsCallRecordingDownloadUrl();
			}else if(UCSAGENTSELECT_URL.equals(code)){
				val = config.getUcsAgentSelectUrl();
			}else if(UCSAGENTADD_URL.equals(code)){
				val = config.getUcsAgentAddUrl();
			}else if(UCSAGENTDELETE_URL.equals(code)){
				val = config.getUcsAgentDeleteUrl();
			}else if(UCSPHONEHANG_URL.equals(code)){
				val = config.getUcsPhoneHangUrl();
			}else if(UCSPHONESTATUS_URL.equals(code)){
				val = config.getUcsPhoneStatusUrl();
			}else if(UCSPHONEBUSY_URL.equals(code)){
				val = config.getUcsPhoneBusyUrl();
			}else if(UCSPHONEFREE_URL.equals(code)){
				val = config.getUcsPhoneFreeUrl();
			}else if(UCSPHONE_KEY.equals(code)){
				val = config.getUcsSpecialChar();
			}else if(UCSSHOWTELNO_URL.equals(code)){
				val = config.getUcsShowTelNoUrl();
			}else if(UCSCALLSTATUS_URL.equals(code)){
				val = config.getUcsCallStatusUrl();
			}else if(UCSUSERDISABLE_URL.equals(code)){
				val = config.getUcsPhoneDisableUrl();
			}else if(UCSUPLOADSHOWNO_URL.equals(code)){
				val = config.getUcsUploadShownoUrl();
			}else if(UCSAGENTUPDATE_URL.equals(code)){
				val = config.getUcsAgentUpdateUrl();
			}else {
				throw new BusinessException(code+"--所请求的参数代码未找到接口");
			}
		}else{
			throw new BusinessException(code+"--系统参数未设置值或者未启用");
		}
		return val ;
	}
	
	/**
	 * 号码格式转换
	 * @param phoneNo
	 * @return
	 */
	public static String phoneNumberConvert(String phoneNo){
		String newNo = "" ;
		String tempNo = "" ;
		String[] phones = phoneNo.split(",");
		for(int i = 0 ; i < phones.length ;i ++){
			tempNo = phones[i] ;
			if("0".equals(phones[i].substring(0,1))){//座机加上国家代号：86-0755-12345678
				tempNo = "86-" + tempNo ; 
			}
			newNo = StringUtils.isEmpty(newNo) ? tempNo : (newNo + "," + tempNo );
		}
		return newNo ;
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
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"GBK"));  
            String line;  
            while ((line = in.readLine()) != null) {  
                msg += line;  
            }  
            in.close();  
            result.put("STATE", "SUCCESS");
            if(msg.contains("-->")){
            	msg=msg.substring(msg.indexOf(">")+1, msg.length());
    		}
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
            
            if(msg.contains("-->")){
            	msg=msg.substring(msg.indexOf(">")+1, msg.length());
    		}
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
		if(UCSCALL_URL.equals(type)){
			if("0".equals(errorKey)){
				msg = "成功"; 
			}else if("-1".equals(errorKey)){
				msg = "余额不足" ;
			}else if("-2".equals(errorKey)){
				msg = "参数不对" ;
			}else if("-3".equals(errorKey)){
				msg = "被叫格式不对" ;
			}else if("-4".equals(errorKey)){
				msg = "预约成功" ;
			}else{
				msg = "其他错误" ;
			}
		}else if(UCSPHONEADD_URL.equals(type)){
			if("0".equals(errorKey)){
				msg = "保存成功";
			}else if("-1".equals(errorKey)){
				msg = "用户存在";
			}else if("-2".equals(errorKey)){
				msg = "创建失败";
			}else if("-3".equals(errorKey)){
				msg = "参数不对";
			}else if("-4".equals(errorKey)){
				msg = "代理商不存在，能创建成功用户归属admin";
			}else if("-5".equals(type)){
				msg = "手机格式不对";
			}
		}else if(UCSPHONEEDIT_URL.equals(type)){
			if("0".equals(errorKey)){
				msg = "修改成功";
			}else if("-1".equals(errorKey)){
				msg = "坐席不存在";
			}else if("-2".equals(errorKey)){
				msg = "坐席号码格式不对";
			}else if("-3".equals(errorKey)){
				msg = "参数不对";
			}else if("-4".equals(errorKey)){
				msg = "坐席类型只能为4或5";
			}
		}else if(UCSLOGIN_URL.equals(type)){
			if("0".equals(errorKey)){
				msg = "登录成功";
			}else if("-1".equals(errorKey)){
				msg = "密码错误";
			}else if("-2".equals(errorKey)){
				msg = "用户不存在";
			}else if("-3".equals(errorKey)){
				msg = "参数不对";
			}
		}else if(UCSAGENTADD_URL.equals(type)){
			if("0".equals(errorKey)){
				msg = "新增成功";
			}else if("-1".equals(errorKey)){
				msg = "创建人不存在";
			}else if("-2".equals(errorKey)){
				msg = "创建的代理已存在";
			}else if("-3".equals(errorKey)){
				msg = "参数不对";
			}
		}else if(UCSPHONEDELETE_URL.equals(type)){
			if("0".equals(errorKey)){
				msg = "删除成功";
			}else if("-1".equals(errorKey)){
				msg = "坐席不存在";
			}else if("-2".equals(errorKey)){
				msg = "参数不对";
			}else if("-3".equals(errorKey)){
				msg = "坐席号码格式不对";
			}
		}else if(UCSPHONEBUSY_URL.equals(type)){
			if("0".equals(errorKey)){
				msg = "示忙成功";
			}else if("-1".equals(errorKey)){
				msg = "客服不存在";
			}else if("-2".equals(errorKey)){
				msg = "参数不对";
			}else if("-3".equals(errorKey)){
				msg = "手机格式不对";
			}
		}else if(UCSPHONEFREE_URL.equals(type)){
			if("0".equals(errorKey)){
				msg = "示闲成功";
			}else if("-1".equals(errorKey)){
				msg = "客服不存在";
			}else if("-2".equals(errorKey)){
				msg = "参数不对";
			}else if("-3".equals(errorKey)){
				msg = "手机格式不对";
			}
		}else if(UCSSHOWTELNO_URL.equals(type)){
			if("0".equals(errorKey)){
				msg = "成功";
			}else if("-1".equals(errorKey)){
				msg = "坐席不存在";
			}else if("-2".equals(errorKey)){
				msg = "用户归属未上传该去电号码";
			}else if("-3".equals(errorKey)){
				msg = "参数不对";
			}else if("-4".equals(errorKey)){
				msg = "修改失败";
			}
		}else if(UCSUPLOADSHOWNO_URL.equals(type)){
			if("0".equals(errorKey)){
				msg = "成功";
			}else if("-1".equals(errorKey)){
				msg = "上传失败，号码被占用";
			}else if("-2".equals(errorKey)){
				msg = "未找到操作人";
			}else if("-3".equals(errorKey)){
				msg = "参数不对";
			}
		}else if(UCSAGENTUPDATE_URL.equals(type)){
			if("0".equals(errorKey)){
				msg = "成功";
			}else if("-1".equals(errorKey)){
				msg = "要修改的经销商不存在";
			}else if("-2".equals(errorKey)){
				msg = "修改失败";
			}else if("-3".equals(errorKey)){
				msg = "参数不对";
			}
		}else if(UCSAGENTDELETE_URL.equals(type)){
			if("0".equals(errorKey)){
				msg = "成功";
			}else if("-1".equals(errorKey)){
				msg = "企业用户不存在";
			}else if("-2".equals(errorKey)){
				msg = "参数不对";
			}
		}
		return msg + "["+errorKey+"]" ;
	}
	public static void main(String[] args) {
		String str="54";
		String ma="^-?\\d+$" ;
		boolean bo=str.matches(ma);
//		System.out.println(bo);
        
	}
}
