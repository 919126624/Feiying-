package com.wuyizhiye.base.export.excel;

import com.wuyizhiye.base.export.DataField;

/**
 * @ClassName ExcelDataField
 * @Description 描述: 
 * @author li.biao
 * @date 2015-4-1
 */
public class ExcelDataField extends DataField {
	
	private int width;
	private CellRender render;
	private CellRender headRender;
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public CellRender getRender() {
		return render;
	}
	public void setRender(CellRender render) {
		this.render = render;
	}
	public CellRender getHeadRender() {
		return headRender;
	}
	public void setHeadRender(CellRender headRender) {
		this.headRender = headRender;
	}
}
