package com.wuyizhiye.basedata.shortcut.enums;

/**
 * @ClassName ShortcutTypeEnum
 * @Description 快捷菜单类型
 * @author li.biao
 * @date 2015-4-3
 */
public enum ShortcutTypeEnum {
	MENU("菜单","MENU"),
	INTERNAL("内部功能","INTERNAL"),
	EXTERNAL("外部地址","EXTERNAL");
	private String label;
	private String value;
	private ShortcutTypeEnum(String label,String value){
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
