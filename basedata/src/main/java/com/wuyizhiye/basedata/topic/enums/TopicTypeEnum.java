package com.wuyizhiye.basedata.topic.enums;

/**
 * @ClassName TopicTypeEnum
 * @Description 话题类型(需求调整 美工优化 数据问题 程序报错 系统建议)
 * @author li.biao
 * @date 2015-4-3
 */
public enum TopicTypeEnum {
	DEMANDS("需求调整","DEMANDS"),
	UI("美工优化","UI"),
	DATA("数据问题","DATA"),
	BUG("程序报错","BUG"),
	PROPOSAL("系统建议","PROPOSAL"),
	
	ERROR_A("需求建议","ERROR_A"),
	ERROR_B("数据问题","ERROR_B"),
	ERROR_C("程序报错","ERROR_C"),
	ERROR_D("其他","ERROR_D")
	;
	private String label;
	private String value;
	private TopicTypeEnum(String label,String value){
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
