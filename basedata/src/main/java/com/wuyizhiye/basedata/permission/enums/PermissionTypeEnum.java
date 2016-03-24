package com.wuyizhiye.basedata.permission.enums;

/**
 * @ClassName PermissionTypeEnum
 * @Description 工作台类型
 * @author li.biao
 * @date 2015-4-3
 */
public enum PermissionTypeEnum {
	MENU("菜单权限","MENU"),
	EFFECT("功能权限","EFFECT");
	private String label;
	private String value;
	private PermissionTypeEnum(String label,String value){
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
