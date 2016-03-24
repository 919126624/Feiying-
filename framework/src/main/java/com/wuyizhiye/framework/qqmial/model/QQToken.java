package com.wuyizhiye.framework.qqmial.model;

import java.io.Serializable;

/**
 * @ClassName QQToken
 * @Description QQ企业邮箱访问证书
 * @author li.biao
 * @date 2015-4-7
 */
public class QQToken  implements Serializable {

	//获取token方式
	public static final String GRANT_TYPE = "client_credentials" ;
	
	//访问Token
	private String access_token  ;
	
	//Token类型
	private String token_type ;
	
	//有效时间
	private int expires_in ;
	
	//刷新token
	private String refresh_token ;

	
	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

}
