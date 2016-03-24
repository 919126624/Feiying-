package com.wuyizhiye.basedata.org.enums;

/**
 * @ClassName OrgStatusEnum
 * @Description 组织状态
 * @author li.biao
 * @date 2015-4-2
 */
public enum OrgStatusEnum{
	ENABLE("启用",0),
	DISABLED("禁用",1);
	private String name;
	private int value;
	private OrgStatusEnum(String name,int value){
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
