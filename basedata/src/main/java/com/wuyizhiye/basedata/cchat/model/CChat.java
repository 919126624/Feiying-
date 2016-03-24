package com.wuyizhiye.basedata.cchat.model;

import java.util.Date;

import org.apache.http.impl.cookie.DateUtils;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName CChat
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class CChat extends CoreEntity {
	private static final long serialVersionUID = 1L;
	//发信人
	private Person sender;
	//收信人
	private Person receiver;
	//内容
	private String content;
	//发送时间
	private Date createTime;
	//状态 （是否阅读）
	private String status;
	//回帖还是主贴
	private String type;
	//主贴ID
	private String topicId;
	
	private String dateStr;
	
	//组织 发送者的组织
	private Org org;
	
	private int unRead;//未读数量(显示字段by xqh)
	
	

	public int getUnRead() {
		return unRead;
	}

	public void setUnRead(int unRead) {
		this.unRead = unRead;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public Person getSender() {
		return sender;
	}

	public void setSender(Person sender) {
		this.sender = sender;
	}

	public Person getReceiver() {
		return receiver;
	}

	public void setReceiver(Person receiver) {
		this.receiver = receiver;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}
	
	public String getCreateTimeStr(){
		return DateUtils.formatDate(createTime,"yyyy-MM-dd HH:mm:ss");
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}
}
