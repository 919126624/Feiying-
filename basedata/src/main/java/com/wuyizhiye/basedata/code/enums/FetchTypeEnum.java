package com.wuyizhiye.basedata.code.enums;

/**
 * @ClassName FetchTypeEnum
 * @Description  打印配置取数方式enum
 * @author li.biao
 * @date 2015-4-2
 */
public enum FetchTypeEnum {
	SQL_PATH("SQL路径","SQL_PATH"),
	SERVICES("服务接口","SERVICES");
	private String label;
	private String value;
	private FetchTypeEnum(String label,String value){
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
