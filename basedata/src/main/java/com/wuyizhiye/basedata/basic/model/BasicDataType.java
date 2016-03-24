package com.wuyizhiye.basedata.basic.model;

import com.wuyizhiye.basedata.TreeEntity;

/**
 * @ClassName BasicDataType
 * @Description 基础数据类型
 * @author li.biao
 * @date 2015-4-2
 */
public class BasicDataType extends TreeEntity<BasicDataType> {
	private static final long serialVersionUID = 133346274381328995L;
	
	public BasicDataType getParent(){
		return this.parent;
	}
	
	public void setParent(BasicDataType parent){
		this.parent = parent;
	}
}
