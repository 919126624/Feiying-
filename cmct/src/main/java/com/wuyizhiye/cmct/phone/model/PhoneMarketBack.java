package com.wuyizhiye.cmct.phone.model;


import java.util.Date;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName PhoneMarketBack
 * @Description 营销状态回调信息实体
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneMarketBack extends CoreEntity {

	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneMarketBackDao";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 关联营销id
	 */
	private PhoneMarket phoneMarket;
	
	/**
	 * 回调报告类型 16,17
	 */
	private String type;
	
	/**
	 * 营销接口任务ID
	 */
	private String workID;
	
	/**
	 * 状态
	 */
	private String callState;
	
	/**
	 * 发送条数（在状态推送结束时推送）,16时用到
	 */
	private String sendCount;
	
	/**
	 * 时间（格式：yyyyMMddHHmmss）
	 */
	private Date stateTime;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public PhoneMarket getPhoneMarket() {
		return phoneMarket;
	}

	public void setPhoneMarket(PhoneMarket phoneMarket) {
		this.phoneMarket = phoneMarket;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWorkID() {
		return workID;
	}

	public void setWorkID(String workID) {
		this.workID = workID;
	}

	public String getCallState() {
		return callState;
	}

	public void setCallState(String callState) {
		this.callState = callState;
	}

	public String getSendCount() {
		return sendCount;
	}

	public void setSendCount(String sendCount) {
		this.sendCount = sendCount;
	}

	public Date getStateTime() {
		return stateTime;
	}

	public void setStateTime(Date stateTime) {
		this.stateTime = stateTime;
	}
}
