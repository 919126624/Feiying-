package com.wuyizhiye.cmct.phone.enums;

/**
 * @ClassName PhoneMemberEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public enum PhoneMemberEnum {

	SHAR("共享","SHAR"),
	PERSONAL("专用","PERSONAL");
	
	private String name;
	private String value;
	private PhoneMemberEnum(String name,String value){
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
