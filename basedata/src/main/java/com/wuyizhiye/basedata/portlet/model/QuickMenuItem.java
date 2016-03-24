package com.wuyizhiye.basedata.portlet.model;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.permission.model.Menu;

/**
 * @ClassName QuickMenuItem
 * @Description 快捷设置  菜单子项
 * @author li.biao
 * @date 2015-4-3
 */
public class QuickMenuItem extends CoreEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3634854204907853646L;
	
	private QuickSet quickSet;//关联主表
	
	private Menu menu;//关联菜单
	
	private String desc;//菜单描述

	public QuickSet getQuickSet() {
		return quickSet;
	}

	public void setQuickSet(QuickSet quickSet) {
		this.quickSet = quickSet;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	

}
