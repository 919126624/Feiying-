package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName PhoneCallRecorDing
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneCallRecorDing extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String mapper = "com.wuyizhiye.cmct.phone.dao.PhoneCallRecorDingDao";
	/**
	 * 计费号码
	 */
	private String costNumber;
	
	/**
	 * 主叫号码
	 */
	private String callerNumber;
	
	/**
	 * 被叫号码
	 */
	private String calleeNumber;
	
	/**
	 * 会话sessionId
	 */
	private String sessionId;
	
	/**
	 * 录音地址
	 */
	private String callRdUrl;
	
	/**
	 * 录音时间
	 */
	private Date callRdDate;
	
	/**
	 * 下载状态
	 */
	private String downStatus;
	
	/**
	 * 录音存储地址(转存的服务器地址)
	 */
	private String saveRdUrl;
	
	/**
	 * 录音临时地址
	 */
	private String slowRdUrl;
	
	/**
	 *不对应字段.录音时长
	 * @return
	 */
	private String callDuration;
	
	public String getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}

	public String getCostNumber() {
		return costNumber;
	}

	public void setCostNumber(String costNumber) {
		this.costNumber = costNumber;
	}

	public String getCallerNumber() {
		return callerNumber;
	}

	public void setCallerNumber(String callerNumber) {
		this.callerNumber = callerNumber;
	}

	public String getCalleeNumber() {
		return calleeNumber;
	}

	public void setCalleeNumber(String calleeNumber) {
		this.calleeNumber = calleeNumber;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getCallRdUrl() {
		return callRdUrl;
	}

	public void setCallRdUrl(String callRdUrl) {
		this.callRdUrl = callRdUrl;
	}

	public Date getCallRdDate() {
		return callRdDate;
	}

	public void setCallRdDate(Date callRdDate) {
		this.callRdDate = callRdDate;
	}

	public String getDownStatus() {
		return downStatus;
	}

	public void setDownStatus(String downStatus) {
		this.downStatus = downStatus;
	}

	public String getSaveRdUrl() {
		return saveRdUrl;
	}

	public void setSaveRdUrl(String saveRdUrl) {
		this.saveRdUrl = saveRdUrl;
	}

	public String getSlowRdUrl() {
		return slowRdUrl;
	}

	public void setSlowRdUrl(String slowRdUrl) {
		this.slowRdUrl = slowRdUrl;
	}
	
}
