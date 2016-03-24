package com.wuyizhiye.basedata.basic.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName ChangYongYu
 * @Description 自定义常用语
 * @author li.biao
 * @date 2015-4-2
 */
public class ChangYongYu extends DataEntity {

	private static final long serialVersionUID = 1L;

	public static final String MAPPER = "com.wuyizhiye.basedata.basic.dao.ChangYongYuDao";

	//关联对象ID
	private String objectId ;
	
	//父类型
	private ChangYongYu parent ;

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public ChangYongYu getParent() {
		return parent;
	}

	public void setParent(ChangYongYu parent) {
		this.parent = parent;
	}
	
	
}
