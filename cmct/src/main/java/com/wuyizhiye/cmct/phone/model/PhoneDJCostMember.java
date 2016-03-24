package com.wuyizhiye.cmct.phone.model;

/**
 * @ClassName PhoneDJCostMember
 * @Description 存值用,获取当前客户的计费号码,httlurl,key,password......
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneDJCostMember {
	
	private String userId ;
	
	private String userKey;
	
	private String httpUrl;
	
	private String spid;
	
	private String appid;
	
	private String passWd;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getSpid() {
		return spid;
	}

	public void setSpid(String spid) {
		this.spid = spid;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPassWd() {
		return passWd;
	}

	public void setPassWd(String passWd) {
		this.passWd = passWd;
	}
}
