package com.wuyizhiye.basedata.permission.model;

import java.util.Date;
import java.util.List;

import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.basedata.TreeEntity;
import com.wuyizhiye.basedata.permission.enums.MenuTypeEnum;

/**
 * @ClassName Menu
 * @Description 菜单
 * @author li.biao
 * @date 2015-4-3
 */
public class Menu extends TreeEntity<Menu> {
	private static final long serialVersionUID = 5273842289637591401L;
	
	/**
	 * URL，系统内菜单为短url：basedata/org/list(request.getServletPath())
	 * 系统外菜单为完整url：http://baidu.com
	 */
	private String link;
	
	/**
	 * 小图标
	 */
	private String miniIcon;
	
	/**
	 * 大图标
	 */
	private String largeIcon;
	
	/**
	 * 权限项
	 */
	private PermissionItem permissionItem;
	
	private String enableFlag;//启用/禁用 标识
	
	/**
	 * 下级菜单
	 */
	private List<Menu> children;
	
	/**
	 * 菜单类型 分手机菜单 电脑菜单
	 * @return
	 */
	private MenuTypeEnum menuType;
	
	private BusinessTypeEnum businessType;
	
	/**
	 * 关联模块枚举
	 */
	private ModuleEnum moduleType;
	
    private String syncType;//数据同步类型 CLOUD为云端数据
	
	private Date syncDate;//数据同步日期
	
	private String cloudId;//对应云上数据ID
	
	public ModuleEnum getModuleType() {
		return moduleType;
	}

	public void setModuleType(ModuleEnum moduleType) {
		this.moduleType = moduleType;
	}

	public Menu getParent(){
		return this.parent;
	}
	
	public void setParent(Menu parent){
		this.parent = parent;
	}

	public void setPermissionItem(PermissionItem permissionItem) {
		this.permissionItem = permissionItem;
	}

	public PermissionItem getPermissionItem() {
		return permissionItem;
	}

	public void setMiniIcon(String miniIcon) {
		this.miniIcon = miniIcon;
	}

	public String getMiniIcon() {
		return miniIcon;
	}

	public void setLargeIcon(String largeIcon) {
		this.largeIcon = largeIcon;
	}

	public String getLargeIcon() {
		return largeIcon;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public MenuTypeEnum getMenuType() {
		return menuType;
	}

	public void setMenuType(MenuTypeEnum menuType) {
		this.menuType = menuType;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public BusinessTypeEnum getBusinessType() {
		return businessType;
	}

	public void setBusinessType(BusinessTypeEnum businessType) {
		this.businessType = businessType;
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
	
//	@Override
//	public boolean isRecordHistory() {
//		return true;
//	}
}
