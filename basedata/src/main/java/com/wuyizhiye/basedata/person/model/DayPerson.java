package com.wuyizhiye.basedata.person.model;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;

/**
 * @ClassName DayPerson
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class DayPerson extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	public static final String MAPPER="com.wuyizhiye.basedata.person.dao.DayPersonDao";
	
	//人员
	private Person person ;
	
	//组织
	private Org org ;
	
	//岗位
	private Job job ;
	
	//职位
	private Position position ;
	
	//生成日期
	private String genDay ;
	
	/**
	 * 是否主要职位：  1表示主要职位，0表示兼职
	 */
	private boolean primary;
	
	
	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getGenDay() {
		return genDay;
	}

	public void setGenDay(String genDay) {
		this.genDay = genDay;
	} 
	
}
