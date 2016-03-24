package com.wuyizhiye.basedata.basic.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName BasicData
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class BasicData extends DataEntity {
	private static final long serialVersionUID = 1289394507689417154L;
	
	private BasicDataType type;
	
	private Boolean enable;

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setType(BasicDataType type) {
		this.type = type;
	}

	public BasicDataType getType() {
		return type;
	}
}
