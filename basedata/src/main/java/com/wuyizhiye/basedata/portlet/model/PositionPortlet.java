package com.wuyizhiye.basedata.portlet.model;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Job;

/**
 * @ClassName PositionPortlet
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class PositionPortlet extends CoreEntity {
	
	Job position;
	HomeSet layout;
	
	public Job getPosition() {
		return position;
	}
	public void setPosition(Job position) {
		this.position = position;
	}
	public HomeSet getLayout() {
		return layout;
	}
	public void setLayout(HomeSet layout) {
		this.layout = layout;
	}
}
