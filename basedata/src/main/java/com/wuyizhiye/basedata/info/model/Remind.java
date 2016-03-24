package com.wuyizhiye.basedata.info.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName Remind
 * @Description  定时提醒
 * @author li.biao
 * @date 2015-4-2
 */
public class Remind extends DataEntity{

	private static final long serialVersionUID = 1L;
	/**
	 * 是否触发
	 */
	private int isRead;
	/**
	 * 提醒时间
	 */
	private Date time;
	/**
	 * 提醒人id
	 */
	private String personIds;
	/**
	 * 提醒标题
	 */
	private String title;
	/**
	 * 提醒内容
	 */
	private String content;
	/**
	 * 点击url
	 */
	private String url;
	
	/**
	 * 对象id
	 */
	private String objId;
	
	/**
	 * 发送渠道
	 */
	private int system;
	private int sms;
	private int weixin;
	
	
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getPersonIds() {
		return personIds;
	}
	public void setPersonIds(String personIds) {
		this.personIds = personIds;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSystem() {
		return system;
	}
	public void setSystem(int system) {
		this.system = system;
	}
	public int getSms() {
		return sms;
	}
	public void setSms(int sms) {
		this.sms = sms;
	}
	public int getWeixin() {
		return weixin;
	}
	public void setWeixin(int weixin) {
		this.weixin = weixin;
	}
	
	
	
	
}
