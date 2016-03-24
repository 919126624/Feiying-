package com.wuyizhiye.basedata.info.enums;

/**
 * @ClassName InfomationStatusEnum
 * @Description 消息提醒状态
 * @author li.biao
 * @date 2015-4-2
 */
public enum InfomationStatusEnum {
	NO_WARN("未提醒","NO_WARN"),
	YES_WARN("已提醒","YES_WARN"),
	YES_CHECK("已查阅","YES_CHECK");
	private String label;
	private String value;
	private InfomationStatusEnum(String label,String value){
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
