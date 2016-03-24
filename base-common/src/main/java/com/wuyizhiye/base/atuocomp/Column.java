package com.wuyizhiye.base.atuocomp;

/**
 * @ClassName Column
 * @Description 列信息
 * @author li.biao
 * @date 2015-4-1
 */
public class Column {
	/**
	 * 列名
	 */
	private String field;
	/**
	 * 唯一标识
	 */
	private boolean key ;
	/**
	 * 显示位置
	 */
	private String align;
	/**
	 * 是否隐藏
	 */
	private boolean hidden;
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	
	public boolean isKey() {
		return key;
	}
	public void setKey(boolean key) {
		this.key = key;
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
