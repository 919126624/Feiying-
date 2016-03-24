package com.wuyizhiye.base.query;

import java.util.List;

/**
 * @ClassName QueryView
 * @Description 查询视图
 * @author li.biao
 * @date 2015-4-1
 * @param <T>
 */
public class QueryView<T> {
	
	/**
	 * 排序序列
	 */
	private List<Sorter> sorters;
	
	/**
	 * 查询字段序列
	 */
	private List<Selector> selectors;
	
	/**
	 * 过滤条件序列
	 */
	private Filter filter;

	public List<Sorter> getSorters() {
		return sorters;
	}

	public void setSorters(List<Sorter> sorters) {
		this.sorters = sorters;
	}

	public List<Selector> getSelectors() {
		return selectors;
	}

	public void setSelectors(List<Selector> selectors) {
		this.selectors = selectors;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
}
