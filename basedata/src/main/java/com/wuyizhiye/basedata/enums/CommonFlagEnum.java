package com.wuyizhiye.basedata.enums;



/**
 * @ClassName CommonFlagEnum
 * @Description 公用标志枚举
 * @author li.biao
 * @date 2015-4-2
 */
public enum CommonFlagEnum {
	
	YES("是","YES"),
	NO("否","NO");
	
	private String label;
	private String value;
	
	private CommonFlagEnum(String label,String value){
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
