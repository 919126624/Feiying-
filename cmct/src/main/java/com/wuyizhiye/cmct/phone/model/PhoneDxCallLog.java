package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.cmct.phone.enums.PhoneDxLogEnum;

/**
 * @ClassName PhoneDxCallLog
 * @Description 记录电信推送进来的 通话状态,录音状态,营销状态,其它错误状态
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneDxCallLog extends CoreEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 日志状态
	 */
	private PhoneDxLogEnum status;
	
	/**
	 * 日志详细
	 */
	private String logDetail;
	
	/**
	 * 当前时间
	 */
	private Date currentDate;
	
	/**
	 * sessionId
	 */
	private String sessionId;
	
	/**
	 * 预留字段
	 */
	private String keepField;

	public PhoneDxLogEnum getStatus() {
		return status;
	}

	public void setStatus(PhoneDxLogEnum status) {
		this.status = status;
	}

	public String getLogDetail() {
		return logDetail;
	}

	public void setLogDetail(String logDetail) {
		this.logDetail = logDetail;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getKeepField() {
		return keepField;
	}

	public void setKeepField(String keepField) {
		this.keepField = keepField;
	}
}
