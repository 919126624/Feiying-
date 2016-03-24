package com.wuyizhiye.base.util;


import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wuyizhiye.base.common.Resp;


/**
 * http请求类
 * @author heluzhu
 */
public class HttpSender
{

    private static final Logger logger = Logger.getLogger(HttpSender.class);
    private static int connectTimeout =  30000;
    private static int timeout =  30000;
  

    /**
     * 
     * 发送请求
     * @return
     */
    public static Resp sendRequest(String basurl, NameValuePair[] nameValuePairs,Resp resp,String type) throws Exception {
        return sendRequest(basurl, nameValuePairs, resp, false,type);
    }
    
    /**
     * 发送请求具体实现类
     * @param basurl	请求url
     * @param nameValuePairs	请求传参 post时使用
     * @param resp				解析
     * @param isGet				是否是get请求
     * @param type				参数已废弃		
     * @return
     * @throws Exception
     */
    public static Resp sendRequest(String basurl, NameValuePair[] nameValuePairs,Resp resp,boolean isGet,String type) throws Exception {
        // 构造HTTP请求
        HttpClient httpclient = new HttpClient();
        HttpMethodBase method = new GetMethod(basurl);
        if (!isGet) {
            method = new PostMethod(basurl);
            if (nameValuePairs != null) {
                ((PostMethod) method).addParameters(nameValuePairs);
            }
        }
        method.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        // 发送请求
        String strResponse = null;
        try {
            httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeout);   
            httpclient.getHttpConnectionManager().getParams().setSoTimeout(timeout);            
            httpclient.executeMethod(method);
            strResponse=IOUtils.toString(method.getResponseBodyAsStream(),"UTF-8" );
        } catch (HttpException e) {
            logger.error("打开 URL失败，URL："  + basurl);
            throw new Exception("打开 URL失败，URL："  + basurl);
        } catch (IOException e) {
            logger.error("打开 URL失败，URL："  + basurl);
            throw new Exception("打开 URL失败，URL："  + basurl);
        } finally {
            method.releaseConnection();            
        }
        
        // debug接收消息内容
        if (logger.isDebugEnabled()) {
            logger.debug("============ url: " + basurl +  " =============");
            logger.debug("============ resp start=============");
            logger.debug(strResponse);
            logger.debug("============ resp end=============");
        }
        
        if (StringUtils.isEmpty(strResponse)) {
            logger.error("SSO返回消息为空，URL："  + basurl);
            throw new Exception("SSO返回消息为空，URL："  + basurl);
        }
        try {
        	resp = resp.praseResp(strResponse);
        	
        } catch (Exception e) {
            logger.error("SSO返回格式错误strResponse="+strResponse,e);
            throw new Exception("SSO返回格式错误");
        } 
        return resp;
        
    }
   

}
