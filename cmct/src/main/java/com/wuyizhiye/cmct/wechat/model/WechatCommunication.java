package com.wuyizhiye.cmct.wechat.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.wechat.enums.WechatCommStatusEnum;

/**
 * @ClassName WechatCommunication
 * @Description 微信交流
 * @author li.biao
 * @date 2015-5-26
 */
public class WechatCommunication extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String MAPPER = "com.wuyizhiye.cmct.wechat.dao.WechatCommunicationDao";
	
	/**
	 * 类型
	 */
	private WechatCommStatusEnum type;
	
	/**
	 * 发送人
	 */
	private Person sendPerson;
	
	/**
	 * 发送时间
	 */
	private Date sendTime;
	
	/**
	 * 接收人
	 */
	private Person receivePerson;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 *内容
	 */
	private String content;
	
	/**
	 * 图片地址
	 */
	private String picUrl;
	
	//以下字段不对应数据库
	private String personIds;

	public String getPersonIds() {
		return personIds;
	}

	public void setPersonIds(String personIds) {
		this.personIds = personIds;
	}

	public WechatCommStatusEnum getType() {
		return type;
	}

	public void setType(WechatCommStatusEnum type) {
		this.type = type;
	}

	public Person getSendPerson() {
		return sendPerson;
	}

	public void setSendPerson(Person sendPerson) {
		this.sendPerson = sendPerson;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Person getReceivePerson() {
		return receivePerson;
	}

	public void setReceivePerson(Person receivePerson) {
		this.receivePerson = receivePerson;
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

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}
