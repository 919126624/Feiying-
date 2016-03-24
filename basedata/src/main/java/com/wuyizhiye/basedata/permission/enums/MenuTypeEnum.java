package com.wuyizhiye.basedata.permission.enums;

/**
 * @ClassName MenuTypeEnum
 * @Description 工作台类型
 * @author li.biao
 * @date 2015-4-3
 */
public enum MenuTypeEnum {
	PC("电脑版","PC"),
	MOBILE("手机版","MOBILE");
	private String label;
	private String value;
	private MenuTypeEnum(String label,String value){
		this.setLabel(label);
		this.setValue(value);
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}
