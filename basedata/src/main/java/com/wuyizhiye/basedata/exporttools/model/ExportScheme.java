package com.wuyizhiye.basedata.exporttools.model;

import java.util.List;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName ExportScheme
 * @Description 数据导出方案
 * @author li.biao
 * @date 2015-4-2
 */
public class ExportScheme extends DataEntity {
	private static final long serialVersionUID = -3320507168504205630L;
	
	/**
	 * 导出sql数据源
	 */
	private String sql;
	
	/**
	 * 包含序号
	 */
	private Boolean useIndex;
	
	private List<ExportFilter> filters;
	
	private List<ExportColumn> columns;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Boolean getUseIndex() {
		return useIndex;
	}

	public void setUseIndex(Boolean useIndex) {
		this.useIndex = useIndex;
	}

	public List<ExportFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<ExportFilter> filters) {
		this.filters = filters;
	}

	public List<ExportColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ExportColumn> columns) {
		this.columns = columns;
	}
}
