package com.wuyizhiye.base.query;

import java.util.List;

/**
 * @ClassName Filter
 * @Description 过滤条件
 * @author li.biao
 * @date 2015-4-1
 */
public class Filter {
	/**
	 * 如果原sql己含过滤条件，以种方式接入过滤,AND 或者是 OR
	 */
	private String joinWith = "AND";
	
	/**
	 * 过滤条件集合
	 */
	private List<FilterItem> filterItems;
	
	/**
	 * 过滤组合式
	 */
	private String masks;

	public String getJoinWith() {
		return joinWith;
	}

	public void setJoinWith(String joinWith) {
		this.joinWith = joinWith;
	}

	public List<FilterItem> getFilterItems() {
		return filterItems;
	}

	public void setFilterItems(List<FilterItem> filterItems) {
		this.filterItems = filterItems;
	}

	public String getMasks() {
		return masks;
	}

	public void setMasks(String masks) {
		this.masks = masks;
	}
}
