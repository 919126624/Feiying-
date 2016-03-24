package com.wuyizhiye.basedata.person.enums;

/**
 * @ClassName UserStatusEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public enum UserStatusEnum {
	ENABLE("启用","ENABLE"),
	DISABLED("禁用","DISABLED");
	private String name;
	private String value;
	private UserStatusEnum(String name,String value){
		this.setName(name);
		this.setValue(value);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}
