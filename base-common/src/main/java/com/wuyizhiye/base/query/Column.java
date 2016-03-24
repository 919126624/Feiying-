package com.wuyizhiye.base.query;

/**
 * @ClassName Column
 * @Description 列信息
 * @author li.biao
 * @date 2015-4-1
 */
public class Column {
	/**
	 * 宽度
	 */
	private int width;
	/**
	 * 列名
	 */
	private String field;
	/**
	 * 表头
	 */
	private String display;
	/**
	 * 显示位置
	 */
	private String align;
	/**
	 * 是否隐藏
	 */
	private boolean hidden;
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
}
