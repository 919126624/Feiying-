package com.wuyizhiye.basedata.permission.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName Shortcuts
 * @Description 快捷方式
 * @author li.biao
 * @date 2015-4-3
 */
public class Shortcuts extends CoreEntity {
	private static final long serialVersionUID = 7154339194660481362L;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 连接
	 */
	private String link;
	
	/**
	 * 对应的菜单
	 */
	private Menu menu;
	
	/**
	 * 序号
	 */
	private int index;
	
	/**
	 * 小图标
	 */
	private String miniIconPath;
	
	/**
	 * 快捷方式图片
	 */
	private String iconPath;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setMiniIconPath(String miniIconPath) {
		this.miniIconPath = miniIconPath;
	}

	public String getMiniIconPath() {
		return miniIconPath;
	}
}
