package com.wuyizhiye.cmct.phone.enums;

/**
 * @ClassName PhoneDxLogEnum
 * @Description 电信日志类型枚举
 * @author li.biao
 * @date 2015-5-26
 */
public enum PhoneDxLogEnum {
	CALLSTATUS("通话状态","CALLSTATUS"),
	RECORDSTATUS("录音状态","RECORDSTATUS"),
	MARKETSTATUS("营销状态","MARKETSTATUS"),
	OTHERSTATUS("其它状态","OTHERSTATUS");
	
	private String name;
	private String value;
	private PhoneDxLogEnum(String name,String value){
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
	public static PhoneDxLogEnum getEnumByValue(String value) {
		PhoneDxLogEnum[] enums = PhoneDxLogEnum.values();
		for (PhoneDxLogEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}
		return null;
	}
}
