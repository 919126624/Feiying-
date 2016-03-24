package com.wuyizhiye.basedata.cusresource.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName CusResource
 * @Description 基础客户来源数据
 * @author li.biao
 * @date 2015-4-2
 */
public class CusResource extends DataEntity {
	private static final long serialVersionUID = 1289394507689417154L;
	
	private CusResource parent;
	
	private Boolean enable;

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean getEnable() {
		return enable;
	}

	public CusResource getParent() {
		return parent;
	}

	public void setParent(CusResource parent) {
		this.parent = parent;
	}

	
}
