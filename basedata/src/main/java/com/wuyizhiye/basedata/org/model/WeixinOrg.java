package com.wuyizhiye.basedata.org.model;

import com.wuyizhiye.basedata.TreeEntity;

/**
 * @ClassName WeixinOrg
 * @Description 组织
 * @author li.biao
 * @date 2015-4-2
 */
public class WeixinOrg extends TreeEntity<WeixinOrg> {
	private static final long serialVersionUID = 1L;
	
	private Position   position ;//查找字段     不对应数据库
	
	
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public WeixinOrg getParent() {
		return this.parent;
	}
	
	public void setParent(WeixinOrg parent) {
		this.parent = parent;
	}
}
