package com.wuyizhiye.base.common;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * @ClassName HttpsVerifyManager
 * @Description HTTPS协议URL请求认证的管理器
 * @Author li.biao
 * @Date 2015-4-1
 */
public class HttpsVerifyManager implements X509TrustManager{
	
	/**
	 * 
	 * <p>Title: </p>  
	 * <p>Description: </p>
	 */
	public HttpsVerifyManager(){
		
	}

	/**
	 * 检查客户端的可信任状态
	 */
	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		
	}

	/**
	 * 检查服务器的可信任状态 
	 */
	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		
	}

	/**
	 * 返回接受的发行商数组
	 */
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		
		return new X509Certificate[] {};
	}

}
