package com.wuyizhiye.basedata.code.enums;

/**
 * @ClassName PriorityEnum
 * @Description 编号优先级别enum
 * @author li.biao
 * @date 2015-4-2
 */
public enum PriorityEnum {
	ORGPRIORITY("组织优先","ORGPRIORITY"),
	GROUPPRIORITY("集团优先","GROUPPRIORITY");
	private String label;
	private String value;
	private PriorityEnum(String label,String value){
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
