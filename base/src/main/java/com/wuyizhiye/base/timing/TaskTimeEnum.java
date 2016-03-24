package com.wuyizhiye.base.timing;

/**
 * @ClassName TaskTimeEnum
 * @Description 任务执行时间时间点
 * @author li.biao
 * @date 2015-4-1
 */
public enum TaskTimeEnum {
	ZERO("ZERO","零点"),
	ONE("ONE","一点"),
	TWO("ZERO","两点"),
	THREE("ZERO","三点"),
	FOUR("ZERO","四点"),
	FIVE("ZERO","五点"),
	SIX("ZERO","六点"),
	OTHER("OTHER","其他");
	private String name;
	private String label;
	private TaskTimeEnum(String name,String label){
		this.label = label;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
