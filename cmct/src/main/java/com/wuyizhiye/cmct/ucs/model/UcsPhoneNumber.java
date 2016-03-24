package com.wuyizhiye.cmct.ucs.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName UcsPhoneNumber
 * @Description 固话号码 （用于话伴接口获取渠道商固话列表）
 * @author li.biao
 * @date 2015-5-26
 */
public class UcsPhoneNumber extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ip
	 */
	private String ip;
	
	/**
	 * callUnit:费率表生成的通话单元
	 */
	private String callUnit;
	
	/**
	 * Caller:计费号码 
	 */
	private String caller;
	
	/**
	 * cardNo:卡号
	 */
	private String cardNo;
	
	/**
	 * connectTime:接通时间
	 */
	private String connectTime;
	
	/**
	 * Duration:通话时长
	 */
	private String duration;
	
	/**
	 * Svcalloutuseroffhoktime:结束时间
	 */
	private String svcalloutuseroffhoktime;
	
	/**
	 * m_svcalloutusertalkmoney:通话费用
	 */
	private String msvcalloutusertalkmoney;
	
	/**
	 * Svcalloutcusconame:客户名称
	 */
	private String svcalloutcusconame;
	
	/**
	 * Svcalloutdispcaller：主叫号码
	 */
	private String svcalloutdispcaller;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCallUnit() {
		return callUnit;
	}

	public void setCallUnit(String callUnit) {
		this.callUnit = callUnit;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getConnectTime() {
		return connectTime;
	}

	public void setConnectTime(String connectTime) {
		this.connectTime = connectTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}


	public String getSvcalloutuseroffhoktime() {
		return svcalloutuseroffhoktime;
	}

	public void setSvcalloutuseroffhoktime(String svcalloutuseroffhoktime) {
		this.svcalloutuseroffhoktime = svcalloutuseroffhoktime;
	}

	public String getMsvcalloutusertalkmoney() {
		return msvcalloutusertalkmoney;
	}

	public void setMsvcalloutusertalkmoney(String msvcalloutusertalkmoney) {
		this.msvcalloutusertalkmoney = msvcalloutusertalkmoney;
	}

	public String getSvcalloutcusconame() {
		return svcalloutcusconame;
	}

	public void setSvcalloutcusconame(String svcalloutcusconame) {
		this.svcalloutcusconame = svcalloutcusconame;
	}

	public String getSvcalloutdispcaller() {
		return svcalloutdispcaller;
	}

	public void setSvcalloutdispcaller(String svcalloutdispcaller) {
		this.svcalloutdispcaller = svcalloutdispcaller;
	}
}
