package com.wuyizhiye.cmct.wechat.enums;

/**
 * @ClassName QRUserEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public enum QRUserEnum {
	PERSON("个人使用", "PERSON"), 
	DITCH("渠道使用", "DITCH");
	
	private String name;
	private String value;
	
	private QRUserEnum(String name ,String value) {
		this.name=name;
		this.value=value;
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
