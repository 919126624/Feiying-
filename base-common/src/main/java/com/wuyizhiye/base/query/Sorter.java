package com.wuyizhiye.base.query;

/**
 * @ClassName Sorter
 * @Description 排序元素
 * @author li.biao
 * @date 2015-4-1
 */
public class Sorter {
	/**
	 * 字段
	 */
	private String field;
	/**
	 * 排序类型
	 */
	private SortType sortType = SortType.ESC;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public SortType getSortType() {
		return sortType;
	}
	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}
}
