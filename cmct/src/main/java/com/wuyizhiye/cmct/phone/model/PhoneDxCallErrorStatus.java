package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName PhoneDxCallErrorStatus
 * @Description 电信电话状态未接受到错误日志
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneDxCallErrorStatus extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public  static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneDxCallErrorStatusDao";

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * sessionid
	 */
	private String sessionId;
	
	/**
	 * errorMsg
	 */
	private String errorMsg;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
