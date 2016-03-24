package com.wuyizhiye.basedata.workload.model;

/**
 * @ClassName WorkloadTypeEnum
 * @Description 工作量类型
 * @author li.biao
 * @date 2015-4-3
 */
public enum WorkloadTypeEnum {
	SOURCE("盘源","SOURCE"),
	OLDHOME("二手房客","OLDHOME"),
	NEWHOME("新房客","NEWHOME") ,
	RESULTS("业绩","RESULTS"),
	TENANT("其他","TENANT");
	private String label;
	private String value;
	private WorkloadTypeEnum(String label,String value){
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
