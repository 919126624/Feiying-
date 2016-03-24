package com.wuyizhiye.basedata.param.enums;

/**
 * @ClassName ParamLevelEnum
 * @Description 编号优先级别enum
 * @author li.biao
 * @date 2015-4-3
 */
public enum ParamLevelEnum {
	GROUP("集团统一","GROUP"),
	COMPANY("公司级","COMPANY"),
	DEPT("部门级","DEPT");
	private String label;
	private String value;
	private ParamLevelEnum(String label,String value){
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
