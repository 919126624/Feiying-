package com.wuyizhiye.basedata.sql.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName ConnectParamBean
 * @Description 连接DB的参数Bean
 * @author li.biao
 * @date 2015-4-3
 */
public class ConnectParamBean  extends DataEntity{
	public static final String MAPPER="com.wuyizhiye.basedata.sql.ConnectParamBeanDao";
	private String id;
	private String name;
	private String dbtype;
	private String user;
	private String password;
	private String url;
	private String dbname;
	private String dbhost;
	private String dbport;

	

	//
	public String toCookieValue() {
		String value = getDbtype() + "#" + getDbhost();
		value += "#" + getDbname() + "#" + getUser();
		// value += "#" + getUrl();
		// +"#"param.getPassword(),不记录密码
		return value;
	}

	public String getHistoryRecord() {
		String value = getDbtype() + "|" + getDbhost();
		value += "|" + getDbname() + "|" + getUser();
		value += "|" + getUrl();
		// +"#"param.getPassword(),不记录密码
		value = "{history:'" + value + "'}";
		return value;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDbhost() {
		return dbhost;
	}

	public void setDbhost(String dbhost) {
		this.dbhost = dbhost;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDbport() {
		return dbport;
	}

	public void setDbport(String dbport) {
		this.dbport = dbport;
	}
}
