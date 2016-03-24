package com.wuyizhiye.framework.listener;

import java.util.Date;

/**
 * @ClassName WashCustomer
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public class WashCustomer {
	
	private String userId;//登录人ID
	private String userName ;//登录人名字
	
	private String customerId ; //客ID
	private String customerName ;//客名字
	
	private Date hangTime ;//占用时间
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Date getHangTime() {
		return hangTime;
	}
	public void setHangTime(Date hangTime) {
		this.hangTime = hangTime;
	}
	
}
