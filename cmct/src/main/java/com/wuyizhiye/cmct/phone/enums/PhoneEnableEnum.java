package com.wuyizhiye.cmct.phone.enums;

/**
 * @ClassName PhoneEnableEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public enum PhoneEnableEnum {
	UNUSE("未使用","UNUSE"),
	USE("在用","USE"),
	DELETE("删除","DELETE"),
	STOP("停用","STOP");
	
	private String name;
	private String value;
	private PhoneEnableEnum(String name,String value){
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
	public static PhoneEnableEnum getEnumByValue(String value) {
		PhoneEnableEnum[] enums = PhoneEnableEnum.values();
		for (PhoneEnableEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}
		return null;
	}
}
