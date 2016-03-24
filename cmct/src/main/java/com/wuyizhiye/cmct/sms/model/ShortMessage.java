package com.wuyizhiye.cmct.sms.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.sms.enums.SMSControlTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSSendTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSStatusEnum;
import com.wuyizhiye.cmct.sms.enums.SMSTypeEnum;

/**
 * @ClassName ShortMessage
 * @Description  短信
 * @author li.biao
 * @date 2015-5-26
 */
public class ShortMessage extends CoreEntity{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 6871331191896848902L;

	
	/**
	 * 创建用户
	 */
	private Person creator;
	
	/**
	 * 数据所属组织
	 */
	private Org org;
	
	private SMSControlTypeEnum  controlType; //控制类型  个人、部门、公司    (必填)
	
	private SMSTypeEnum type ;//短信类型  业务、广告   (必填)
	
	private String senderId ;//发送人   (必填)
	
	private String senderNumber ;//发送人编码   (必填)
	
	private String senderName ;//发送人名称    (必填)
	
	private String senderPhoneNum ;//发送手机号码   (必填)
	
	private String receiverName ;//接收人名称     (多个名称之间用半角逗号隔开)    (必填)
	 
	private String receiverPhoneNum ;//接收人手机号码   (多个号码之间用半角逗号隔开)   (必填)
	
	private String content ;//短信内容   (必填)
	
	private Date sendTime; //发送时间    (为空时立即发送，不为空时 定时发送)
	
	private String description;     //短信描述  (短信服务提供商(Service BeanId)+接口名称)
	
	private Integer sendSmsCount ;//发送短信条数
	
	private String taskId;//任务ID
	
	private SMSStatusEnum status;//短信发送状态(发送成功 还是发送失败)
	
	private Date createTime;
	
	private SMSSendTypeEnum sendType;//短信类别
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public SMSSendTypeEnum getSendType() {
		return sendType;
	}

	public void setSendType(SMSSendTypeEnum sendType) {
		this.sendType = sendType;
	}

	public SMSStatusEnum getStatus() {
		return status;
	}

	public void setStatus(SMSStatusEnum status) {
		this.status = status;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public SMSControlTypeEnum getControlType() {
		return controlType;
	}

	public void setControlType(SMSControlTypeEnum controlType) {
		this.controlType = controlType;
	}

	public SMSTypeEnum getType() {
		return type;
	}

	public void setType(SMSTypeEnum type) {
		this.type = type;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderNumber() {
		return senderNumber;
	}

	public void setSenderNumber(String senderNumber) {
		this.senderNumber = senderNumber;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderPhoneNum() {
		return senderPhoneNum;
	}

	public void setSenderPhoneNum(String senderPhoneNum) {
		this.senderPhoneNum = senderPhoneNum;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhoneNum() {
		return receiverPhoneNum;
	}

	public void setReceiverPhoneNum(String receiverPhoneNum) {
		this.receiverPhoneNum = receiverPhoneNum;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSendSmsCount() {
		return sendSmsCount;
	}

	public void setSendSmsCount(Integer sendSmsCount) {
		this.sendSmsCount = sendSmsCount;
	}
	
}
