package com.wuyizhiye.base.atuocomp;

import java.util.List;

/**
 * @ClassName AutoComp
 * @Description 自动补全工具
 * @author li.biao
 * @date 2015-4-1
 * @param <T>
 */
public class AutoComp<T> {
	/**
	 * 查询的ID
	 */
	private String id;
	/**
	 * 对应查询的mapper
	 */
	private String mapper;
	/**
	 * 列设置
	 */
	private List<Column> columns;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMapper() {
		return mapper;
	}
	public void setMapper(String mapper) {
		this.mapper = mapper;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
	
	
}
