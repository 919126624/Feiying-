package com.wuyizhiye.base.timing;

/**
 * @ClassName TaskLastStatusEnum
 * @Description 描述: 任务最后执行状态
 * @author li.biao
 * @date 2015-4-1
 */
public enum TaskLastStatusEnum {
	
	/**
	 * 己执行
	 */
	RUNED("RUNED","执行成功"),
	
	/**
	 * 执行失败
	 */
	FAIL("FAIL","执行失败");
	
	private String name;
	private String label;
	private TaskLastStatusEnum(String name,String label){
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
