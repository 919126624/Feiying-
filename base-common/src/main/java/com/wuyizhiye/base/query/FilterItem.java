package com.wuyizhiye.base.query;

/**
 * @ClassName FilterItem
 * @Description 过滤项，field匹配顺序：resultMap -> select -> 直接写
 * @author li.biao
 * @date 2015-4-1
 */
public class FilterItem {
	private String field;
	private Object value;
	private CompareType compareType = CompareType.EQUALS;
	public FilterItem(String field,Object value){
		this.field = field;
		this.value = value;
	}
	public FilterItem(String field,Object value,CompareType compareType){
		this.field = field;
		this.value = value;
		this.compareType = compareType;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public CompareType getCompareType() {
		return compareType;
	}
	public void setCompareType(CompareType compareType) {
		this.compareType = compareType;
	}
}
