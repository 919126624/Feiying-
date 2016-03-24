package com.wuyizhiye.base.query;

import java.util.List;

/**
 * @ClassName Query
 * @Description 查询工具
 * @author li.biao
 * @date 2015-4-1
 * @param <T>
 */
public class Query<T> {
	/**
	 * 查询的ID
	 */
	private String id;
	/**
	 * 对应查询的mapper
	 */
	private String mapper;
	
	private QueryView<T> view;
	
	/**
	 * 列设置
	 */
	private List<Column> columns;

	public String getMapper() {
		return mapper;
	}

	public void setMapper(String mapper) {
		this.mapper = mapper;
	}

	public QueryView<T> getView() {
		return view;
	}

	public void setView(QueryView<T> view) {
		this.view = view;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	/**
	 * @return the columns
	 */
	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
}
