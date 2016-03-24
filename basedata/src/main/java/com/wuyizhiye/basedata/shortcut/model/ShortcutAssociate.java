package com.wuyizhiye.basedata.shortcut.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName ShortcutAssociate
 * @Description 人员与快捷菜单的对应
 * @author li.biao
 * @date 2015-4-3
 */
public class ShortcutAssociate extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	
	private String personId;//人员岗位id
	private String shortcutId;//可用快捷菜单
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getShoutcutId() {
		return shortcutId;
	}
	public void setShoutcutId(String shortcutId) {
		this.shortcutId = shortcutId;
	}

	
}
