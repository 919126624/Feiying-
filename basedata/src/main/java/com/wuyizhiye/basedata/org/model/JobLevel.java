package com.wuyizhiye.basedata.org.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName JobLevel
 * @Description 职级
 * @author li.biao
 * @date 2015-4-2
 */
public class JobLevel extends DataEntity {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 岗位
	 */
	private Job job;
	
	/**
	 * 级别
	 */
	private Integer level;
	
	public JobLevel(){
		
	}
	
	public JobLevel(String id){
		super.setId(id);
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Job getJob() {
		return job;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getLevel() {
		return level;
	}
}
