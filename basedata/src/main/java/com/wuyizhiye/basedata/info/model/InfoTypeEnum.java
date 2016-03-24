package com.wuyizhiye.basedata.info.model;

/**
 * @ClassName InfoTypeEnum
 * @Description 消息类型
 * @author li.biao
 * @date 2015-4-2
 */
public enum InfoTypeEnum {
	INTERACTIVE("互动消息","INTERACTIVE"),
	SYSTEM("系统消息","SYSTEM"),
	BUSINESS("业务提醒","BUSINESS"),
	PROCESS("流程消息","PROCESS");
	private String label;
	private String value;
	private InfoTypeEnum(String label,String value){
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
