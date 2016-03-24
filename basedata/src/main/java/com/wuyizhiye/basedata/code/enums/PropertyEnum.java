package com.wuyizhiye.basedata.code.enums;

/**
 * @ClassName PropertyEnum
 * @Description 编号规则属性enum
 * @author li.biao
 * @date 2015-4-2
 */
public enum PropertyEnum {
	FIXEDVALUE("固定值","FIXEDVALUE"),
	FORMATDATE("日期","FORMATDATE"),
	SEQUENCE("顺序号","SEQUENCE"),
	ORGCODE("组织编码","ORGCODE"),
	ORGABBREVIATION("组织简称","ORGABBREVIATION");
	private String label;
	private String value;
	private PropertyEnum(String label,String value){
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
