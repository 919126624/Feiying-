package com.wuyizhiye.base.query;

/**
 * @ClassName CompareType
 * @Description 过滤条件比较类型
 * @author li.biao
 * @date 2015-4-1
 */
public enum CompareType {
	EQUALS("等于","="),
	IS("是","IS"),
	IS_NOT("不是","IS NOT"),
	NOT_EQUALS("不等于","<>"),
	LARGE("大于",">"),
	LESS("小于","<"),
	LARGE_EQUALS("大于等于","<="),
	LESS_EQUALS("小于等于","<="),
	IN("在","IN"),
	NOT_IN("不在","NOT IN"),
	EXISTS("存在","EXISTS");
	private String name;
	private String value;
	private CompareType(String name,String value){
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
