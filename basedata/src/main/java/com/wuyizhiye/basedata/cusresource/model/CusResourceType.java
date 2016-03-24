package com.wuyizhiye.basedata.cusresource.model;

import com.wuyizhiye.basedata.TreeEntity;

/**
 * @ClassName CusResourceType
 * @Description 基础客户来源数据类型
 * @author li.biao
 * @date 2015-4-2
 */
public class CusResourceType extends TreeEntity<CusResourceType> {
	private static final long serialVersionUID = 133346274381328995L;
	
	public CusResourceType getParent(){
		return this.parent;
	}
	
	public void setParent(CusResourceType parent){
		this.parent = parent;
	}
}
