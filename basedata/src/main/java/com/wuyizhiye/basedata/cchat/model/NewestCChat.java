package com.wuyizhiye.basedata.cchat.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.cchat.enums.InfoTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName NewestCChat
 * @Description 聊聊最新消息，最后内容以及未读数量
 * @author li.biao
 * @date 2015-4-2
 */
public class NewestCChat extends CoreEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6586986683664999556L;
	
	private Person person;//私信 关联人员(发送人)
	
	private Person belong;//私信  (接收人)
	
	private Date time;//最新的时间
	
	private String phone;//短信  
	
	private String content;//最后一次 通信内容
	
	private int unRead;//几条未读
	
	private InfoTypeEnum type;//数据类别
	
	private int isRemind;//是否已经弹出提醒  1 是 0 否
	
	
	public int getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(int isRemind) {
		this.isRemind = isRemind;
	}

	public int getUnRead() {
		return unRead;
	}

	public void setUnRead(int unRead) {
		this.unRead = unRead;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Person getBelong() {
		return belong;
	}

	public void setBelong(Person belong) {
		this.belong = belong;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public InfoTypeEnum getType() {
		return type;
	}

	public void setType(InfoTypeEnum type) {
		this.type = type;
	}
	
	

}
