package com.wuyizhiye.basedata.topic.enums;

/**
 * @ClassName TopicLevelEnum
 * @Description 话题级别
 * @author li.biao
 * @date 2015-4-3
 */
public enum TopicLevelEnum {
	LEVEL_A("A类:严重错误","LEVEL_A"),
	LEVEL_B("B类:关键错误","LEVEL_B"),
	LEVEL_C("C类:轻微错误","LEVEL_C"),
	LEVEL_D("D类:需求意见或建议","LEVEL_D");
	private String label;
	private String value;
	private TopicLevelEnum(String label,String value){
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
