/**
 * com.wuyizhiye.basedata.org.enums.OrgStatusEnum.java
 */
package com.wuyizhiye.hr.enums;

/**
 * 
 * @author FengMy
 * 
 * @since 2012-9-21
 */
public enum StatusEnum{
	ENABLE("启用",0),
	DISABLED("禁用",1);
	private String name;
	private int value;
	private StatusEnum(String name,int value){
		this.setName(name);
		this.setValue(value);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
