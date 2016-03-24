package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

/**
 * @ClassName PhoneNumber
 * @Description 固话号码 （用于话伴接口获取渠道商固话列表）
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneNumber {

	//号码
	private String phoneNo ;
	
	//号码类型
	private String phoneType ;
	
	//使用套餐
	private String currentPackName ;
	
	//所属组织
	private String orgName ;
	
	//所属用户（注册手机号）
	private String mobileNo ;
	
	//状态 1：在使用（已购买已分配）2：在使用（已购买未分配）
	//3．欠费停用（已购买）4：组织所有号码（已购买的所有号码）5：在售（所属渠道商在售的号码）
	private int status ;
	
	//购买时间
	private Date buyTime ;
	
	//停用时间
	private Date stopTime ;

	
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getCurrentPackName() {
		return currentPackName;
	}

	public void setCurrentPackName(String currentPackName) {
		this.currentPackName = currentPackName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	
}
