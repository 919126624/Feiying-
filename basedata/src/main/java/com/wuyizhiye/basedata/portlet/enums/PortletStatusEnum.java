package com.wuyizhiye.basedata.portlet.enums;

/**
 * @ClassName PortletStatusEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public enum PortletStatusEnum{
	ENABLE("启用",0),
	DISABLED("禁用",1);
	private String name;
	private int value;
	private PortletStatusEnum(String name,int value){
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
