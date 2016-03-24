package com.wuyizhiye.basedata.notetemplate.model;

import com.wuyizhiye.base.CoreEntity;


/**
 * @ClassName NoteTemplateEntry
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class NoteTemplateEntry extends CoreEntity{
		
	
	/**
	 * 字段名称
	 */
	private String name;
	/**
	 * 字段名
	 */
	private String field;
	/**
	 * 关联票据模板ID
	 */
	private String parentId;
	
	private Integer index;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}




	
	
}
