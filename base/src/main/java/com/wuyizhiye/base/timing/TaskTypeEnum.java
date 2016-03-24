package com.wuyizhiye.base.timing;

/**
 * @ClassName TaskTypeEnum
 * @Description 任务类型
 * @author li.biao
 * @date 2015-4-1
 */
public enum TaskTypeEnum {
	/**
	 * 单次任务
	 */
	ONCE("ONCE","单次任务"),
	/**
	 * 每天循环 原循环任务
	 */
	CYCLE("CYCLE","每天循环"),
	/**
	 * 每月循环
	 */
	MONTH("MONTH","每月循环");
	
	private String name;
	private String label;
	private TaskTypeEnum(String name,String label){
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
