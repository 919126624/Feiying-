package com.wuyizhiye.basedata.cchat.enums;

/**
 * @ClassName InfoTypeEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public enum InfoTypeEnum {
	PRIVATE_LETTER("私信","PRIVATE_LETTER"),
	SHORT_MESSAGE("短信","SHORT_MESSAGE"),;
	private String name;
	
	private String value;
	
	private InfoTypeEnum(String name,String value){
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
