package com.wuyizhiye.basedata.weChat.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName WeChatConfig
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class WeChatConfig extends CoreEntity{
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 名字描述
	 */
	private String name;
	
	/**
	 * 对应url
	 */
	private String url;
	/**
	 * 编码（关联的基础）
	 */
	private String number;
	/**
	 * 也用于关联
	 */
	private String appId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
}
