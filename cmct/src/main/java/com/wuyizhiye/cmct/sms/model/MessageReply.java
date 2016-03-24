package com.wuyizhiye.cmct.sms.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.cmct.sms.enums.SMSTypeEnum;

/**
 * @ClassName MessageReply
 * @Description 短信回复信息
 * @author li.biao
 * @date 2015-5-26
 */
public class MessageReply extends CoreEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5143360196218080678L;
	
	private String mobile;//回复人电话
	
	private String taskId;//对应任务ID
	
	private Date receivetime;//回复时间
	
	private String content;//回复 内容
	
	private SMSTypeEnum type;//区分类型
	
	/**
	 * 非表中字段
	 * @return
	 */
	
	private String sendContent;//发送内容
	
	private String receiverName;//接收人姓名
	
	private Date sendTime;//发送时间
	
	private CmctContact contact;//判断是否在通讯录中
	
	public CmctContact getContact() {
		return contact;
	}

	public void setContact(CmctContact contact) {
		this.contact = contact;
	}

	public SMSTypeEnum getType() {
		return type;
	}

	public void setType(SMSTypeEnum type) {
		this.type = type;
	}

	public String getSendContent() {
		return sendContent;
	}

	public void setSendContent(String sendContent) {
		this.sendContent = sendContent;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

   
	public Date getReceivetime() {
		return receivetime;
	}

	public void setReceivetime(Date receivetime) {
		this.receivetime = receivetime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

}
