package com.wuyizhiye.basedata.exporttools.model;

import com.wuyizhiye.base.export.DataField;

/**
 * @ClassName ExportColumn
 * @Description 导出列设置
 * @author li.biao
 * @date 2015-4-2
 */
public class ExportColumn extends DataField {
	private static final long serialVersionUID = 4167364278100713634L;
	/**
	 * 方案
	 */
	private ExportScheme scheme;
	/**
	 * 顺序
	 */
	private Integer index;
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public ExportScheme getScheme() {
		return scheme;
	}
	public void setScheme(ExportScheme scheme) {
		this.scheme = scheme;
	}
}
