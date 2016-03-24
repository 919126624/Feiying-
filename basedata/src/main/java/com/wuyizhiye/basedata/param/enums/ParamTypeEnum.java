package com.wuyizhiye.basedata.param.enums;

/**
 * @ClassName ParamTypeEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public enum ParamTypeEnum {
	STRING("字符串类型","STRING"),
	BOOL("布尔类型","BOOL"),
	INT("整数类型","INT"),
	NUMBER("金额类型","NUMBER");
	private String label;
	private String value;
	private ParamTypeEnum(String label,String value){
		this.label = label;
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
