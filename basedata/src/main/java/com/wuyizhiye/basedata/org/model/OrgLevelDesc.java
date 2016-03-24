package com.wuyizhiye.basedata.org.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName OrgLevelDesc
 * @Description 组织级别表
 * @author li.biao
 * @date 2015-4-2
 */
public class OrgLevelDesc extends CoreEntity {
	
	private String name; //组织级别名
	private Integer seq;//组织级别序号
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}
