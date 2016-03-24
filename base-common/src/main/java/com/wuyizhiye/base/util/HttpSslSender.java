package com.wuyizhiye.base.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;

import com.wuyizhiye.base.common.HttpsVerifyManager;
import com.wuyizhiye.base.common.Resp;


/**
 * ssl http请求类
 * @author Administrator
 *
 */
public class HttpSslSender {
	
    private static final Logger logger = Logger.getLogger(HttpSslSender.class);
    private static int connectTimeout =  30000;
    private static int timeout =  30000;
	
    public static Resp sendRequest(String basurl, Resp resp) throws Exception {
    	return sendRequest(basurl, resp,"utf-8");
    }
    
   /***
    * 发生请求类
    * @param basurl		发生请求url
    * @param resp		数据解析类
    * @param chartset	字符集
    * @return
    * @throws Exception
    */
    public static Resp sendRequest(String basurl, Resp resp,String chartset) throws Exception {
        // 构造HTTP请求
    	class TrustAnyHostnameVerifier implements HostnameVerifier {
			@Override
			public boolean verify(String hostname, SSLSession sslSession) {
				// TODO Auto-generated method stub
				return true;
			}
		}
		
		String msg = "";   
        try {
        	
        	URL url = new URL(basurl);
        	
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
        	conn.setRequestProperty("Charset", chartset);
        	conn.setConnectTimeout(connectTimeout);
        	conn.setReadTimeout(timeout);
        	conn.connect(); 
            
            //构建流，获取返回值
        	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),chartset));  
        	String line;  
            while ((line = in.readLine()) != null) {  
                msg += line;  
            }  
            in.close();            
        } catch (Exception e) {  
        	 logger.error(e.getMessage());
             throw new Exception("打开 URL失败，URL："  + basurl);
        }
        try {
        	resp = resp.praseResp(msg);
        	
        } catch (Exception e) {
            logger.error("SSL返回格式错误strResponse="+msg,e);
            throw new Exception("SSO返回格式错误");
        } 
        return resp;  
        
    }
}
