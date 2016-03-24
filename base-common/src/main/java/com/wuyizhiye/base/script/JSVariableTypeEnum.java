package com.wuyizhiye.base.script;

/**
 * @ClassName JSVariableTypeEnum
 * @Description  JS变量类型
 * @author li.biao
 * @date 2015-4-1
 */
public enum JSVariableTypeEnum {
	STRING("STRING","字符串"),
	NUMBER("NUMBER","数值"),
	INT("INT","整型"),
	FLOAT("FLOAT","浮点型"),
	BOOLEAN("BOOLEAN","布尔");
	private String name;
	private String label;
	private JSVariableTypeEnum(String name,String label){
		this.name = name;
		this.label = label;
	}
	public String getName() {
		return name;
	}
	public String getLabel() {
		return label;
	}
}
