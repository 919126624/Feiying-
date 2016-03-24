package com.wuyizhiye.cmct.phone.model;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName PhoneDeputyNum
 * @Description 副号实体,
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneDeputyNum extends DataEntity {

	
	private static final long serialVersionUID = 1L;

	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneDeputyNumDao";
	
	/**
	 * 当前绑定人员
	 */
	private Person bindPerson;
	
	/**
	 * 副号
	 */
	private String billNumber;

	public Person getBindPerson() {
		return bindPerson;
	}

	public void setBindPerson(Person bindPerson) {
		this.bindPerson = bindPerson;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
}
