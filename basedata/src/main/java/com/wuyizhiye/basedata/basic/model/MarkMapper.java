package com.wuyizhiye.basedata.basic.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName MarkMapper
 * @Description 标签关联映射表
 * @author li.biao
 * @date 2015-4-2
 */
public class MarkMapper extends CoreEntity {
	
	private static final long serialVersionUID = 1;
	
	public static final String MAPPER = "com.wuyizhiye.basedata.basic.dao.MarkMapperDao";
	
	//关联映射对象ID
	private String mapperId ;
	
	//关联标签
	private String markId ;
	
	//创建时间
	private Date ctime ;
	
	//创建人
	private Person creator ;
	
	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public String getMapperId() {
		return mapperId;
	}

	public void setMapperId(String mapperId) {
		this.mapperId = mapperId;
	}

	public String getMarkId() {
		return markId;
	}

	public void setMarkId(String markId) {
		this.markId = markId;
	}
	
}
