package com.wuyizhiye.base.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/**
 * @ClassName HttpClientUtil
 * @Description 调用URL工具类，可以处理HTTP协议和HTTPS协议的请求
 * @author li.biao
 * @date 2015-4-1
 */
public class HttpClientUtil {

	private static Log log = LogFactory.getLog(HttpClientUtil.class);

	public static String callHttpUrl(String url, String paramStr ,boolean isSelfParam ,Map<String,String> params){
		return callHttpUrl(url, paramStr ,isSelfParam ,params,0);
	}
	/**
	 * 调用URL , HTTP POST 请求
	 * @param url 
	 * @param paramStr 例如：name=test&password=test
	 * @param isSelfParam 为true时 设置params参数到请求url中
	 * @param params
	 * @return
	 */
	public static String callHttpUrl(String url, String paramStr ,boolean isSelfParam ,Map<String,String> params,int time) {
		
		//响应内容
		String returnContent = "";
		
		//创建默认的httpClient实例
		HttpClient client = new DefaultHttpClient();
		HttpParams hp = client.getParams();
		if(time<=0){
			HttpConnectionParams.setConnectionTimeout(hp, 10000);
			HttpConnectionParams.setSoTimeout(hp, 10000);
		}else{
			HttpConnectionParams.setConnectionTimeout(hp, time);
			HttpConnectionParams.setSoTimeout(hp, time);
		}
		try {
			//创建HttpPost
			HttpPost post = new HttpPost(url);
			post.setHeader("content-type", "charset=UTF-8");
			StringEntity strEntity;		
			//设置参数
			if(isSelfParam){
				List<NameValuePair> formParams = new ArrayList<NameValuePair>(); 
				for (Map.Entry<String, String> entry : params.entrySet()) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry
							.getValue()));
				}
				HttpEntity paramEntity = new UrlEncodedFormEntity(formParams);
				post.setEntity(paramEntity);
			}else{
				strEntity = new StringEntity(paramStr, "UTF-8");
				post.setEntity(strEntity);
			}
			
			//执行请求 得到响应
			HttpResponse response = client.execute(post);
			
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), "UTF-8"));
				StringBuffer buff = new StringBuffer();
				while ((returnContent = reader.readLine()) != null) {
					buff.append(returnContent);
				}
				returnContent = buff.toString();
				post.abort();
			}
		} catch (Exception e) {
//			e.printStackTrace();
			log.error("",e);
			log.error("HttpClientUtil.callHttpUrl 出错：" + e.getMessage());
		} finally {
			//关闭连接，释放资源
			client.getConnectionManager().shutdown();
		}
		return returnContent;
	}
	
	/**
	 * 调用URL , HTTP POST 请求
	 * @param url
	 * @param paramStr 例如：name=test&password=test
	 * @return
	 */
	public static String callHttpUrl(String url, String paramStr) {
		if(paramStr == null){
			paramStr = "" ;
		}
		return callHttpUrl(url, paramStr,false, null);
	}

	/**
	 * 调用URL , HTTP POST 请求
	 * @param url
	 * @param paramStr 例如：name=test&password=test
	 * @return
	 */
	public static String callHttpUrl(String url, String paramStr,int time) {
		if(paramStr == null){
			paramStr = "" ;
		}
		return callHttpUrl(url, paramStr,false, null,time);
	}
	/**
	 * 以POST方式发送单向 HTTPS请求
	 * @param url
	 * @param paramStr
	 * @param isSelfParam
	 *            是否需要设置参数 true时 params参数值设置到请求中
	 * @param params
	 * @return
	 */
	public static String callHttpsUrl(String url, String paramStr,
			boolean isSelfParam, Map<String, String> params) {
		
		// 响应内容
		String returnContent = ""; 
		
		// 创建默认的httpClient实例
		HttpClient httpClient = new DefaultHttpClient();
		
		// 创建TrustManager
		X509TrustManager xtm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}
		};
		try {
			SSLContext ctx = SSLContext.getInstance("SSL");

			// 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);

			// 创建SSLSocketFactory
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);

			// 通过SchemeRegistry将SSLSocketFactory注册到HttpClient上 ； 443为HTTPS默认端口
			httpClient.getConnectionManager().getSchemeRegistry()
					.register(new Scheme("https", 443, socketFactory));

			// 创建HttpPost
			HttpPost httpPost = new HttpPost(url); 
			
			// 构建POST请求的表单参数
			if (isSelfParam) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>(); 
				for (Map.Entry<String, String> entry : params.entrySet()) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry
							.getValue()));
				}
				HttpEntity paramEntity = new UrlEncodedFormEntity(formParams);
				httpPost.setEntity(paramEntity);
			}else{
				httpPost.setEntity(new StringEntity(paramStr, "UTF-8"));
			}
			
			// 执行POST请求
			HttpResponse response = httpClient.execute(httpPost); 
			// 获取响应实体
			HttpEntity entity = response.getEntity();

			if (null != entity) {
				returnContent = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity); // Consume response content
			}
			/*
			log.debug("callHttpsUrl 请求地址: " + httpPost.getURI());
			log.debug("callHttpsUrl 响应状态: " + response.getStatusLine());
			log.debug("callHttpsUrl 响应内容: " + returnContent);
			
			System.out.println("callHttpsUrl 请求地址: " + httpPost.getURI());
			System.out.println("callHttpsUrl 响应状态: " + response.getStatusLine());
			System.out.println("callHttpsUrl 响应内容: " + returnContent);
			*/
		} catch (Exception e) {
			log.error("HttpClientUtil.callHttpsUrl 出错：" + e.getMessage());
//			e.printStackTrace();
			log.error("",e);
		} finally {
			// 关闭连接,释放资源
			httpClient.getConnectionManager().shutdown(); 
		}
		return returnContent;
	}

	/**
	 * 以POST方式发送单向 HTTPS请求
	 * 
	 * @param url
	 * @param paramStr
	 * @return
	 */
	public static String callHttpsUrl(String url, String paramStr) {
		if(paramStr == null){
			paramStr = "" ;
		}
		return callHttpsUrl(url, paramStr, false, null);
	}
	
	/**
	 * 以GET方式发送HTTP请求
	 * @param urlString
	 * @return
	 */
	public static String callHttpUrlGet(String urlString)  {  
		String msg = "";   
        try {
        	//打开连接
            URL url = new URL(urlString);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setDoOutput(true);  
            conn.setRequestMethod("GET");  
            
            //构建流，获取返回值
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));  
            String line;  
            while ((line = in.readLine()) != null) {  
                msg += line;  
            }  
            in.close();  
        } catch (Exception e) {  
        	log.error("HttpClientUtil.callHttpUrlGet 出错：" + e.getMessage());
//            e.printStackTrace();
        	log.error("",e);
        }  
        return msg;  
    }  

	
	/**
	 * 调用URL , HTTP POST 请求
	 * @param url 
	 * @param paramStr 例如：name=test&password=test
	 * @param isSelfParam 为true时 设置params参数到请求url中
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String callHttpUrlThrowEx(String url, String paramStr ,boolean isSelfParam ,Map<String,String> params) throws ClientProtocolException, IOException {
		
		//响应内容
		String returnContent = "";
		
		//创建默认的httpClient实例
		HttpClient client = new DefaultHttpClient();
		HttpParams hp = client.getParams();
		HttpConnectionParams.setConnectionTimeout(hp, 10000);
		HttpConnectionParams.setSoTimeout(hp, 10000);
		
			//创建HttpPost
			HttpPost post = new HttpPost(url);
			post.setHeader("content-type", "charset=UTF-8");
			StringEntity strEntity;		
			//设置参数
			if(isSelfParam){
				List<NameValuePair> formParams = new ArrayList<NameValuePair>(); 
				for (Map.Entry<String, String> entry : params.entrySet()) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry
							.getValue()));
				}
				HttpEntity paramEntity = new UrlEncodedFormEntity(formParams);
				post.setEntity(paramEntity);
			}else{
				strEntity = new StringEntity(paramStr, "UTF-8");
				post.setEntity(strEntity);
			}
			
			//执行请求 得到响应
			HttpResponse response = client.execute(post);
			
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), "UTF-8"));
				StringBuffer buff = new StringBuffer();
				while ((returnContent = reader.readLine()) != null) {
					buff.append(returnContent);
				}
				returnContent = buff.toString();
				post.abort();
			}	
			client.getConnectionManager().shutdown();
		return returnContent;
	}
	
	/**
	 * 调用URL , HTTP POST 请求
	 * @param url
	 * @param paramStr 例如：name=test&password=test
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String callHttpUrlThrowEx(String url, String paramStr) throws ClientProtocolException, IOException {
		if(paramStr == null){
			paramStr = "" ;
		}
		return callHttpUrlThrowEx(url, paramStr,false, null);
	}
}
