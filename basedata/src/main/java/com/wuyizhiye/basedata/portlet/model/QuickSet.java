package com.wuyizhiye.basedata.portlet.model;

import java.util.List;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;

/**
 * @ClassName QuickSet
 * @Description 快捷设置
 * @author li.biao
 * @date 2015-4-3
 */
public class QuickSet extends CoreEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3130183588372197580L;
	
	private String name;//名称
	
    private String menuName;//菜单冗余名称
    
    private String jobName;//岗位冗余名称
    
    private CommonFlagEnum isDefault;//是否默认
    
    private CommonFlagEnum isAble;//是否可用
    
    /**
     * 非表中字段
     * @return
     */
    private List<QuickMenuItem> menuList;//菜单
    
    private List<QuickJobItem> jobList;//岗位
    
    private String menuJson;//菜单json数据
    
//    private String jobJson;
    
    
    

	public List<QuickMenuItem> getMenuList() {
		return menuList;
	}

	public String getMenuJson() {
		return menuJson;
	}

	public void setMenuJson(String menuJson) {
		this.menuJson = menuJson;
	}

	public void setMenuList(List<QuickMenuItem> menuList) {
		this.menuList = menuList;
	}

	public List<QuickJobItem> getJobList() {
		return jobList;
	}

	public void setJobList(List<QuickJobItem> jobList) {
		this.jobList = jobList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public CommonFlagEnum getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(CommonFlagEnum isDefault) {
		this.isDefault = isDefault;
	}

	public CommonFlagEnum getIsAble() {
		return isAble;
	}

	public void setIsAble(CommonFlagEnum isAble) {
		this.isAble = isAble;
	}
    
    
    
}
