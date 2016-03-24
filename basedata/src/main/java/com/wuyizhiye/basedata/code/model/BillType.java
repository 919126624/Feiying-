package com.wuyizhiye.basedata.code.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.permission.model.PermissionItem;

/**
 * @ClassName BillType
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class BillType extends DataEntity {

	private static final long serialVersionUID = 1L;

	private String module; // 类型 FKMODULEID
	/**
	 * 新增界面
	 */
	private String addLink;

	/**
	 * 编辑界面
	 */
	private String editLink;

	/**
	 * 查看界面
	 */
	private String viewLink;

	/**
	 * 单据删除链接
	 */
	private String deleteLink;

	/**
	 * 新增页面大小 宽X高 必须以X号隔开 例如：803X100
	 */
	private String addSize;

	/**
	 * 编辑页面大小 例如：803X100
	 */
	private String editSize;

	/**
	 * 查看页面大小 例如：803X100
	 */
	private String viewSize;
	
	private PermissionItem permissionItem;// 关联权限项
	private int idx;// 排序号
	private String defalutIcon;// 默认图标
	private String checkedIcon;// 选中后的图标
	private String disabledIcon;// 禁用图标

	private int isHaveAuthority;// 是否具有权限 查询字段 不存在数据表中

	/**
	 * 移动版查看链接
	 * 
	 */
	private String mobileViewLink;
	
    private String syncType;//数据同步类型 CLOUD为云端数据
	
	private Date syncDate;//数据同步日期
	
	private String cloudId;//对应云上数据ID

	public String getMobileViewLink() {
		return mobileViewLink;
	}

	public void setMobileViewLink(String mobileViewLink) {
		this.mobileViewLink = mobileViewLink;
	}

	public String getDisabledIcon() {
		return disabledIcon;
	}

	public void setDisabledIcon(String disabledIcon) {
		this.disabledIcon = disabledIcon;
	}

	public int getIsHaveAuthority() {
		return isHaveAuthority;
	}

	public void setIsHaveAuthority(int isHaveAuthority) {
		this.isHaveAuthority = isHaveAuthority;
	}

	public PermissionItem getPermissionItem() {
		return permissionItem;
	}

	public void setPermissionItem(PermissionItem permissionItem) {
		this.permissionItem = permissionItem;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getDefalutIcon() {
		return defalutIcon;
	}

	public void setDefalutIcon(String defalutIcon) {
		this.defalutIcon = defalutIcon;
	}

	public String getCheckedIcon() {
		return checkedIcon;
	}

	public void setCheckedIcon(String checkedIcon) {
		this.checkedIcon = checkedIcon;
	}

	public String getDeleteLink() {
		return deleteLink;
	}

	public void setDeleteLink(String deleteLink) {
		this.deleteLink = deleteLink;
	}

	public String getAddSize() {
		return addSize;
	}

	public void setAddSize(String addSize) {
		this.addSize = addSize;
	}

	public String getEditSize() {
		return editSize;
	}

	public void setEditSize(String editSize) {
		this.editSize = editSize;
	}

	public String getViewSize() {
		return viewSize;
	}

	public void setViewSize(String viewSize) {
		this.viewSize = viewSize;
	}

	private String deploymentId; // Activity流程部署ID 查询字段

	private String version; // 版本号 查询字段

	private String resourceName;// 流程资源 查询字段

	private String diagramResourceName;// 流程图 查询字段

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getDiagramResourceName() {
		return diagramResourceName;
	}

	public void setDiagramResourceName(String diagramResourceName) {
		this.diagramResourceName = diagramResourceName;
	}

	public String getAddLink() {
		return addLink;
	}

	public void setAddLink(String addLink) {
		this.addLink = addLink;
	}

	public String getEditLink() {
		return editLink;
	}

	public void setEditLink(String editLink) {
		this.editLink = editLink;
	}

	public String getViewLink() {
		return viewLink;
	}

	public void setViewLink(String viewLink) {
		this.viewLink = viewLink;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
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
