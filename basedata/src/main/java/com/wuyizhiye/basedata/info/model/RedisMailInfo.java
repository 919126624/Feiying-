package com.wuyizhiye.basedata.info.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName RedisMailInfo
 * @Description 用于存放到redis的邮件实体
 * @author li.biao
 * @date 2015-4-2
 */
public class RedisMailInfo extends CoreEntity {

	private String personId;			//人员Id
	private int mailCount;				//消息数量
	private Boolean mailNeedFloatDlg;	//消息是否需要弹窗
	private String mailLastContent;		//消息最后内容
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public int getMailCount() {
		return mailCount;
	}
	public void setMailCount(int mailCount) {
		this.mailCount = mailCount;
	}
	public Boolean getMailNeedFloatDlg() {
		return mailNeedFloatDlg;
	}
	public void setMailNeedFloatDlg(Boolean mailNeedFloatDlg) {
		this.mailNeedFloatDlg = mailNeedFloatDlg;
	}
	public String getMailLastContent() {
		return mailLastContent;
	}
	public void setMailLastContent(String mailLastContent) {
		this.mailLastContent = mailLastContent;
	}
	
	
}
