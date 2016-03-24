package com.wuyizhiye.basedata.portlet.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName PortletLayout
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class PortletLayout extends CoreEntity {
	private HomeSet parent;
	private Portlet portlet;
	private Integer layoutX;
	private Integer layoutY;
	
	public HomeSet getParent() {
		return parent;
	}
	public void setParent(HomeSet parent) {
		this.parent = parent;
	}
	public Portlet getPortlet() {
		return portlet;
	}
	public void setPortlet(Portlet portlet) {
		this.portlet = portlet;
	}
	public Integer getLayoutX() {
		return layoutX;
	}
	public void setLayoutX(Integer layoutX) {
		this.layoutX = layoutX;
	}
	public Integer getLayoutY() {
		return layoutY;
	}
	public void setLayoutY(Integer layoutY) {
		this.layoutY = layoutY;
	}
	
}
