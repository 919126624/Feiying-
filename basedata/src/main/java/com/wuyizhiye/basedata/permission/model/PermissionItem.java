package com.wuyizhiye.basedata.permission.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.permission.enums.PermissionTypeEnum;

/**
 * @ClassName PermissionItem
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class PermissionItem extends DataEntity {
	private static final long serialVersionUID = -3065431772660180837L;
	
	/**
	 * 权限所属分组
	 */
	private PermissionGroup group;
	
	private String module; //FKMODULEID 模块类型
	
	/**
	 * 权限对应方法:如com.wuyizhiye.basedata.permission.controller.PermissionListController.add
	 */
	private String method;

	private PermissionTypeEnum permissionType;
	
	private PermissionItem menuPerm;
	
	private String syncType;//数据同步类型 CLOUD为云端数据
	
	private Date syncDate;//数据同步日期
	
	private String cloudId;//对应云上数据ID
	
	public PermissionGroup getGroup() {
		return group;
	}

	public void setGroup(PermissionGroup group) {
		this.group = group;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return method;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public PermissionTypeEnum getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(PermissionTypeEnum permissionType) {
		this.permissionType = permissionType;
	}
	
	public String getPermissionTypeDesc(){
		return null==this.getPermissionType()?"":this.getPermissionType().getLabel();
	}

	public PermissionItem getMenuPerm() {
		return menuPerm;
	}

	public void setMenuPerm(PermissionItem menuPerm) {
		this.menuPerm = menuPerm;
	}

	public String getSyncType() {
		return syncType;
	}

	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}

	public Date getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(Date syncDate) {
		this.syncDate = syncDate;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

}
