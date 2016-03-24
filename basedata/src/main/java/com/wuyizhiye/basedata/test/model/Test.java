package com.wuyizhiye.basedata.test.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName Test
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class Test extends DataEntity  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7543702519617814690L;
	
	/**
	 * 用户姓名
	 */
	private String userName;
	
	/**
	 * 用户电话
	 */
	private String phoneNumber;
	
	/**
	 * 用户意向级别
	 */
	private int level;


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
