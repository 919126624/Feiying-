package com.wuyizhiye.basedata.permission.model;

import com.wuyizhiye.basedata.TreeEntity;

/**
 * @ClassName PermissionGroup
 * @Description 权限分组
 * @author li.biao
 * @date 2015-4-3
 */
public class PermissionGroup extends TreeEntity<PermissionGroup> {
	private static final long serialVersionUID = 2021484573803780586L;

	public void setParent(PermissionGroup parent) {
		this.parent = parent;
	}

	public PermissionGroup getParent() {
		return this.parent;
	}
}
