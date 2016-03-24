package com.wuyizhiye.basedata.exporttools.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName ExportFilter
 * @Description 导出数据过滤条件
 * @author li.biao
 * @date 2015-4-2
 */
public class ExportFilter extends CoreEntity {
	private static final long serialVersionUID = -7848109780151740983L;
	
	/**
	 * 所属方案
	 */
	private ExportScheme scheme;
	
	/**
	 * 条件名
	 */
	private String name;
	
	/**
	 * 条件编码
	 */
	private String number;
	
	/**
	 * 过滤条件类型
	 */
	private FilterTypeEnum filterType;
	
	/**
	 * 如果filter = DATAPICKER,则此字段必须填
	 */
	private String dataPicker;
	
	/**
	 * 行编号
	 */
	private Integer index;

	public ExportScheme getScheme() {
		return scheme;
	}

	public void setScheme(ExportScheme scheme) {
		this.scheme = scheme;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDataPicker() {
		return dataPicker;
	}

	public void setDataPicker(String dataPicker) {
		this.dataPicker = dataPicker;
	}

	public FilterTypeEnum getFilterType() {
		return filterType;
	}

	public void setFilterType(FilterTypeEnum filterType) {
		this.filterType = filterType;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
}
