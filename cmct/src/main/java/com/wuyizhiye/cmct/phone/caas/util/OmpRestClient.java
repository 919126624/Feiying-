package com.wuyizhiye.cmct.phone.caas.util;

import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @ClassName OmpRestClient
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class OmpRestClient {
	/**
	 * DEFAULT_SERVER = "205.177.226.80:18083";
	 */
	public static final String DEFAULT_SERVER = "205.177.226.80:18083";
	
	private static final String HTTPS_PREFIX = "https://";
	private static final String OAUTH_TOKEN_API = "/omp/oauth/token";
	private static final String OAUTH_FASTLOGIN_API = "/rest/fastlogin?ver=1.0&";
	private static final String OAUTH_TRIGGER_API = "/rest/httpsessions?ver=1.0&";
	private static final String OAUTH_TEST_TRIGGER_API = "/sandbox/rest/httpsessions?ver=1.0&";
	private static final String CLICK_TO_CALL_API = "/sandbox/rest/httpsessions/click2Call?ver=1.0&";
	
	private String tokenUrl = "";
	private String fastloginUrl = "";
	private String triggerUrl = "";
	private String testTriggerUrl = "";
	private String click2callUrl = "";
	private String redirectUri = "";
	private String appSecret = "";
	private String appKey = "";
	
	private int timeout = 30000;
	
	public OmpRestClient(String serverAddress, String redirectUri, String appKey, String appSecret) {
		this.tokenUrl = HTTPS_PREFIX + serverAddress + OAUTH_TOKEN_API;
		this.fastloginUrl = HTTPS_PREFIX + serverAddress + OAUTH_FASTLOGIN_API;
		this.triggerUrl = HTTPS_PREFIX + serverAddress + OAUTH_TRIGGER_API;
		this.testTriggerUrl = HTTPS_PREFIX + serverAddress + OAUTH_TEST_TRIGGER_API;
		this.click2callUrl = HTTPS_PREFIX + serverAddress + CLICK_TO_CALL_API;
		this.redirectUri = redirectUri;
		this.appKey = appKey;
		this.appSecret = appSecret;
	}
	
	public OmpRestClient(String redirectUri, String appKey, String appSecret) {
		this(DEFAULT_SERVER, redirectUri, appKey, appSecret);
	}
		
	public void switchToTestTriggerUrl() {
		this.triggerUrl = testTriggerUrl;
	}
	
	public String getToken(String code) {
		if(null == code || code.equals("") || 
				null == tokenUrl || tokenUrl.equals("") ||
				null == redirectUri || redirectUri.equals("") || 
				null == appKey || appKey.equals("") || 
				null == appSecret || appSecret.equals("")) {
			return "getToken: Invalid Input Parameters";
		}
		InputStream inputStream=null;
		DataOutputStream out= null;
		ByteArrayOutputStream baos = null;
		String data = "";
		try {
		    //Https
		    HttpsURLConnection conn = getHttpsPostConnection(tokenUrl);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			String encodedRedirectUri = java.net.URLEncoder.encode(redirectUri, "UTF-8");
			String str = "grant_type=authorization_code&code=" + code + "&redirect_uri=" + encodedRedirectUri 
					+ "&app_key=" + appKey + "&app_secret=" + appSecret;
			byte[] sendData = str.getBytes();
			int length = sendData.length;
			conn.setRequestProperty("Content-Length", String.valueOf(length));
			conn.connect();
			out = new DataOutputStream(conn.getOutputStream());
			out.write(sendData);
			
			int responseCode = conn.getResponseCode();
			if(responseCode == 200) {
				inputStream = conn.getInputStream();
				baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024*4];
				int readSize = -1;
				while((readSize = inputStream.read(buffer)) != -1) {
					baos.write(buffer, 0, readSize);
				}
				data = baos.toString();
				return data;
			}
			
		} catch (Exception e) {
		} finally{
			try {
				if(inputStream!=null)
					inputStream.close();
				if(out != null)
					out.close();
				if(baos!=null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
		
	}
	
	public String getFastloginToken(String username, String password) {
		InputStream inputStream=null;
		DataOutputStream out= null;
		ByteArrayOutputStream baos = null;
		String data = "";
		String accessToken = "";
		try {
		    //timestamp
			String timestamp = getTimestamp();
			
			//sign
			String md5Path = "app_key=" + appKey + "&username=" + username + 
			"&password=" + password + "&timestamp=" + timestamp + "&app_secret=" + appSecret;
			String sign = CaasMD5.getStringMD5(md5Path);

			//prepare post url
			String userName = username.replace("+", "%2b");
			String url = fastloginUrl + "app_key=" + appKey +
			"&username=" + userName + "&timestamp=" + timestamp + "&format=json&sign=" + sign;
			
			//get https connection
			HttpsURLConnection conn = getHttpsPostConnection(url);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			conn.setRequestProperty("Authorization", password);
			conn.connect();

			int responseCode = conn.getResponseCode();
			
			if(responseCode == 200) {
				inputStream = conn.getInputStream();
				baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024*4];
				int readSize = -1;
				while((readSize = inputStream.read(buffer)) != -1){
					baos.write(buffer, 0, readSize);
				}
				data = baos.toString();
				if(null != data && !data.trim().equals("")) {
					accessToken = parseJsonForToken(data);
				} else {
				}
			} else {
				inputStream = conn.getErrorStream();
				baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024*4];
				int readSize = -1;
				while((readSize = inputStream.read(buffer)) != -1){
					baos.write(buffer, 0, readSize);
				}
				data = baos.toString();
			}
		} catch (Exception e) {
		} finally{
			try {
				if(inputStream!=null)
					inputStream.close();
				if(out != null)
					out.close();
				if(baos!=null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return accessToken;
	}
	
	public String triggerApp(String token, HashMap<String, String> appData) {
		if(null == token || token.equals("") 
				|| null == triggerUrl || triggerUrl.equals("") 
				|| null == appKey || appKey.equals("")
				|| null == appSecret || appSecret.equals("")) {
			return "triggerApp: invalid token";
		}
		InputStream inputStream=null;
		DataOutputStream out= null;
		ByteArrayOutputStream baos = null;
		String data = "Failed";
		try {
			//prepare request url
			//timestamp
			String timestamp = getTimestamp();
			
			//md5
			String md5Path = "app_key=" + appKey + "&access_token=" + token 
					+ "&timestamp=" + timestamp + "&app_secret=" + appSecret;
			String sign = CaasMD5.getStringMD5(md5Path);
			String requestUrl = triggerUrl + "app_key=" + appKey + "&access_token=" + token 
					+ "&timestamp=" + timestamp + "&format=json&sign=" + sign;
			
			//prepare request body
			String requestBody = "{ \"servicedata\":\"action=create";
			if(null != appData) {
				for(String key : appData.keySet()) {
					requestBody = requestBody + "&" + key + "=" + appData.get(key);
				}
			}			
			requestBody = requestBody + "\" }";
			
		    //start https connection
			HttpsURLConnection conn = getHttpsPostConnection(requestUrl);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			byte[] sendData = requestBody.getBytes();

			conn.connect();
			out = new DataOutputStream(conn.getOutputStream());
			out.write(sendData);
			
			int responseCode = conn.getResponseCode();
			if(responseCode == 200)
			{
				inputStream = conn.getInputStream();
				baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024*4];
				int readSize = -1;
				while((readSize = inputStream.read(buffer)) != -1){
					baos.write(buffer, 0, readSize);
				}
				data = baos.toString();
				return data;
			}
			
		} catch (Exception e) {
			data = e.toString();
		} finally{
			try {
				if(inputStream!=null)
					inputStream.close();
				if(out != null)
					out.close();
				if(baos!=null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return data;
		
	}
	
	public String triggerApp(String token, String appData) {
		if(null == token || token.equals("") 
				|| null == triggerUrl || triggerUrl.equals("") 
				|| null == appKey || appKey.equals("")
				|| null == appSecret || appSecret.equals("")) {
			return "Invalid token";
		}
		InputStream inputStream=null;
		DataOutputStream out= null;
		ByteArrayOutputStream baos = null;
		String data = "Failed";
		try {
			//prepare request url
			//timestamp
			String timestamp = getTimestamp();
			
			//md5
			String md5Path = "app_key=" + appKey + "&access_token=" + token 
					+ "&timestamp=" + timestamp + "&app_secret=" + appSecret;
			String sign = CaasMD5.getStringMD5(md5Path);
			String requestUrl = triggerUrl + "app_key=" + appKey + "&access_token=" + token 
					+ "&timestamp=" + timestamp + "&format=json&sign=" + sign;
			
			//prepare request body
			String requestBody = "{ \"servicedata\":\"action=create" + appData + "\" }";
			
		    //start http connection
			HttpsURLConnection conn = getHttpsPostConnection(requestUrl);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			byte[] sendData = requestBody.getBytes();

			conn.connect();
			out = new DataOutputStream(conn.getOutputStream());
			out.write(sendData);
			
			int responseCode = conn.getResponseCode();
			if(responseCode == 200) {
				inputStream = conn.getInputStream();
				baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024*4];
				int readSize = -1;
				while((readSize = inputStream.read(buffer)) != -1){
					baos.write(buffer, 0, readSize);
				}
				data = baos.toString();
				data = "Success";
			} else {
				inputStream = conn.getErrorStream();
				baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024*4];
				int readSize = -1;
				while((readSize = inputStream.read(buffer)) != -1){
					baos.write(buffer, 0, readSize);
				}
				data = baos.toString();
				data = "Failed";
			}
		} catch (Exception e) {
			data = e.toString();
		} finally{
			try {
				if(inputStream!=null)
					inputStream.close();
				if(out != null)
					out.close();
				if(baos!=null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
		
	}
	
	public Map<String, Object> triggerClick2call(String token, String caller, String callee, String display) {
		Map<String, Object>result=new HashMap<String, Object>();
        if(null == token || token.equals("") 
                || null == click2callUrl || click2callUrl.equals("") 
                || null == appKey || appKey.equals("")
                || null == appSecret || appSecret.equals("")) {
        	result.put("STATE", "FAIL");
        	result.put("MSG", "Invalid token / app param");
            return result;
        }
        
        if(null == caller || caller.equals("") 
                || null == callee || callee.equals("") 
                || null == display || display.equals("")) {
        	result.put("STATE", "FAIL");
        	result.put("MSG", "Invalid number(s)");
            return result;
        }
        
        InputStream inputStream=null;
        DataOutputStream out= null;
        ByteArrayOutputStream baos = null;
        String data = "Failed";
        try {
            String requestUrl = click2callUrl + "app_key=" + appKey + "&access_token=" + token + "&format=json";
            
            JSONObject rootObject = new JSONObject();
            rootObject.element("callRecord", "0");
            rootObject.element("callerNbr", caller);
            rootObject.element("calleeNbr", callee);
            rootObject.element("displayNbr", display);
            rootObject.element("languageType", "1");
            
            String requestBody = rootObject.toString();
            
            HttpsURLConnection conn = getHttpsPostConnection(requestUrl);
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            byte[] sendData = requestBody.getBytes();

            conn.connect();
            out = new DataOutputStream(conn.getOutputStream());
            out.write(sendData);
            
            int responseCode = conn.getResponseCode();
            if(responseCode == 200) {
                inputStream = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024*4];
                int readSize = -1;
                while((readSize = inputStream.read(buffer)) != -1){
                    baos.write(buffer, 0, readSize);
                }
                JSONObject jsonObj=JSONObject.fromObject(baos.toString());
                result.put("STATE", "SUCCESS");
            	result.put("sessionId", jsonObj.getString("sessionid"));
            } else {
                inputStream = conn.getErrorStream();
                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024*4];
                int readSize = -1;
                while((readSize = inputStream.read(buffer)) != -1){
                    baos.write(buffer, 0, readSize);
                }
                data = baos.toString();
                result.put("STATE", "FAIL");
            	result.put("MSG", data);
            }
        } catch (Exception e) {
        	result.put("STATE", "FAIL");
        	result.put("MSG", "Invalid token / app param");
        } finally{
            try {
                if(inputStream!=null)
                    inputStream.close();
                if(out != null)
                    out.close();
                if(baos!=null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
        
    }
	
	public String getTokenString(String code) {
		String dataString = getToken(code);
		String accessToken = "";
		if(null != dataString && !dataString.equals("")) {
			try {
				accessToken = parseJsonForToken(dataString);
			} catch (Exception e) {
			}
		}
		return accessToken;
	}
	
	public String parseJsonForToken(String jsonString) {
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			String accessToken = jsonObject.getString("access_token");
			return accessToken;
	}
	
	private String getTimestamp() {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		df.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
		String timestamp = df.format(date);
		return timestamp;
	}
	
	private void setupSSL() throws Exception {
		SSLContext sc = SSLContext.getInstance("TLS");
	    sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	    HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
	}
	
	private HttpsURLConnection getHttpsPostConnection(String url) throws Exception {
		//SSL
		setupSSL();
		
		URL ul = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) ul.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(getServerTimeout());
		conn.setReadTimeout(getServerTimeout());
		conn.setDoInput(true);
		conn.setDoOutput(true);
		return conn;
	}
	
	public int getServerTimeout() {
		return this.timeout;
	}
	
	public boolean setServerTimeout(int value) {
		if(value < 5000 || value > 120000) {
			return false;
		}
		this.timeout = value;
		return true;
	}

	 private class MyHostnameVerifier implements HostnameVerifier{

         @Override
         public boolean verify(String hostname, SSLSession session) {
        	 return true;
         }

	 }

	 private class MyTrustManager implements X509TrustManager{

         @Override
         public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
         }
         
         @Override
         public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
         }

         @Override
         public X509Certificate[] getAcceptedIssuers() {
            return null;
         }        

	 }

}
