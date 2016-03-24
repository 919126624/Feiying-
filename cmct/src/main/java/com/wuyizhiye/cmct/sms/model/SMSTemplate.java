package com.wuyizhiye.cmct.sms.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.sms.enums.SMSTemplateEnum;

/**
 * @ClassName SMSTemplate
 * @Description 短信模板
 * @author li.biao
 * @date 2015-5-26
 */
public class SMSTemplate extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String MAPPER = "com.wuyizhiye.cmct.sms.dao.SMSTemplateDao";
	/**
	 * 短信内容
	 */
	private String smsContent;
	
	/**
	 * 创建人
	 */
	private Person person;
	
	/**
	 * 模板编码
	 */
	private String number;
	
	/**
	 * 状态
	 */
	private SMSTemplateEnum templateStatus;
	
	/**
	 * 创建时间
	 */
	private Date createTime;

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public SMSTemplateEnum getTemplateStatus() {
		return templateStatus;
	}

	public void setTemplateStatus(SMSTemplateEnum templateStatus) {
		this.templateStatus = templateStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
