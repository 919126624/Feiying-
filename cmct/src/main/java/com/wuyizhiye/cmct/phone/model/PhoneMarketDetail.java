package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName PhoneMarketDetail
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneMarketDetail extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneMarketDetailDao";
	
	/**
	 * 关联营销状态回调信息实体
	 */
	private PhoneMarketBack phoneMarketBack;

	/**
	 * 关联营销id
	 */
	private PhoneMarket phoneMarket;
	
	/**
	 * 营销接口任务ID
	 */
	private String workID;
	
	/**
	 * 被叫号码
	 */
	private String calleeNbr;
	
	/**
	 * SESSIONID
	 */
	private String sessionId;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 接听时长
	 */
	private String callDuration;
	
	/**
	 * 转接时长
	 */
	private String transferDuration;
	
	/**
	 * 转接号码
	 */
	private String transferNumber;
	
	/**
	 * 开始接听时间
	 */
	private Date startTimeO;
	
	/**
	 * 开始接听时间
	 */
	private Date startTimeT;
	
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	/**
	 * 按键值,
	 */
	private String keyValue;
	
	/**
	 * 创建时间
	 */
	private Date createTime;

	public PhoneMarketBack getPhoneMarketBack() {
		return phoneMarketBack;
	}

	public void setPhoneMarketBack(PhoneMarketBack phoneMarketBack) {
		this.phoneMarketBack = phoneMarketBack;
	}

	public PhoneMarket getPhoneMarket() {
		return phoneMarket;
	}

	public void setPhoneMarket(PhoneMarket phoneMarket) {
		this.phoneMarket = phoneMarket;
	}

	public String getWorkID() {
		return workID;
	}

	public void setWorkID(String workID) {
		this.workID = workID;
	}

	public String getCalleeNbr() {
		return calleeNbr;
	}

	public void setCalleeNbr(String calleeNbr) {
		this.calleeNbr = calleeNbr;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}

	public String getTransferDuration() {
		return transferDuration;
	}

	public void setTransferDuration(String transferDuration) {
		this.transferDuration = transferDuration;
	}

	public String getTransferNumber() {
		return transferNumber;
	}

	public void setTransferNumber(String transferNumber) {
		this.transferNumber = transferNumber;
	}

	public Date getStartTimeO() {
		return startTimeO;
	}

	public void setStartTimeO(Date startTimeO) {
		this.startTimeO = startTimeO;
	}

	public Date getStartTimeT() {
		return startTimeT;
	}

	public void setStartTimeT(Date startTimeT) {
		this.startTimeT = startTimeT;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
