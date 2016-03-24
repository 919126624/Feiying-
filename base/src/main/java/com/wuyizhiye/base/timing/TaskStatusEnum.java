package com.wuyizhiye.base.timing;

/**
 * @ClassName TaskStatusEnum
 * @Description 任务状态
 * @author li.biao
 * @date 2015-4-1
 */
public enum TaskStatusEnum {
	/**
	 * 待执行
	 */
	WAIT("WAIT","待执行"),
	
	/**
	 * 己执行
	 */
	RUNED("RUNED","执行成功"),
	
	/**
	 * 执行失败
	 */
	FAIL("FAIL","执行失败"),
	
	/**
	 * 己取消
	 */
	CANCLE("CANCLE","己取消");
	private String name;
	private String label;
	private TaskStatusEnum(String name,String label){
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
