package com.wuyizhiye.framework.qqmial.model;


/**
 * @ClassName QQMail
 * @Description QQ企业邮箱工具类 存放需要的账号和连接信息
 * @author li.biao
 * @date 2015-4-7
 */
public class QQMail {

	/**
	 * 获取OAuth验证授权链接
	 */
	public static final String URL_GETOAUTH = "https://exmail.qq.com/cgi-bin/token" ;
	
	/**
	 * 获取登陆/读信 Key
	 */
	public static final String URL_GETAUTHKEY = "http://openapi.exmail.qq.com:12211/openapi/mail/authkey" ;
	
	/**
	 * 邮箱单点登陆
	 */
	public static final String URL_SSOLOGIN = "https://exmail.qq.com/cgi-bin/login?fun=bizopenssologin&method=bizauth" ;
	
	/**
	 * 获取新邮件数
	 */
	public static final String URL_NEWCOUNT = "http://openapi.exmail.qq.com:12211/openapi/mail/newcount" ;
	
	/**
	 * 同步部门
	 */
	public static final String URL_PARTYSYNC = "http://openapi.exmail.qq.com:12211/openapi/party/sync" ;
	
	/**
	 * 同步成员
	 */
	public static final String URL_USERSYNC = "http://openapi.exmail.qq.com:12211/openapi/user/sync" ;
	
	public static final String url_sid = "https://exmail.qq.com/cgi-bin/login"; 
	
	
	//管理员id
	private String client_id  ;
	
	//管理员密匙
	private String client_secret ;
	
	//证书
	private QQToken token ;

	
	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public QQToken getToken() {
		return token;
	}

	public void setToken(QQToken token) {
		this.token = token;
	}

}
