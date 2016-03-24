package com.wuyizhiye.cmct.phone.model;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.phone.enums.PhoneMobileStatusEnum;

/**
 * @ClassName PhonemMobileMember
 * @Description 移动版,电话线路
 * @author li.biao
 * @date 2015-5-26
 */
public class PhonemMobileMember extends DataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhonemMobileMemberDao";
	
	/**
	 * 手机号
	 */
	private String phoneNum;

	/**
	 * 号码状态
	 */
	private PhoneMobileStatusEnum status;
	
	/**
	 * 对应人
	 */
	private Person person;
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public PhoneMobileStatusEnum getStatus() {
		return status;
	}

	public void setStatus(PhoneMobileStatusEnum status) {
		this.status = status;
	}
}
