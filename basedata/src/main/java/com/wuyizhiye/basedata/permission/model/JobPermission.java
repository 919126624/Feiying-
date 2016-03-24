package com.wuyizhiye.basedata.permission.model;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Job;

/**
 * @ClassName JobPermission
 * @Description 岗位权限
 * @author li.biao
 * @date 2015-4-3
 */
public class JobPermission extends CoreEntity {
	private static final long serialVersionUID = 1813614650628034427L;
	
	private Job job;
	
	private PermissionItem permissionItem;

	public void setJob(Job job) {
		this.job = job;
	}

	public Job getJob() {
		return job;
	}

	public void setPermissionItem(PermissionItem permissionItem) {
		this.permissionItem = permissionItem;
	}

	public PermissionItem getPermissionItem() {
		return permissionItem;
	}
}
