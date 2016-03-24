package com.wuyizhiye.basedata.org.enums;

/**
 * @ClassName WorkBenchTypeEnum
 * @Description 工作台类型
 * @author li.biao
 * @date 2015-4-2
 */
public enum WorkBenchTypeEnum {
	SIMPLE("折叠式","simple"),
	ICON("平铺式","icon");
	private String label;
	private String value;
	private WorkBenchTypeEnum(String label,String value){
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
