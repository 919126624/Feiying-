package com.wuyizhiye.basedata.portlet.model;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Job;

/**
 * @ClassName QuickJobItem
 * @Description 快捷设置  岗位子项
 * @author li.biao
 * @date 2015-4-3
 */
public class QuickJobItem extends CoreEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6534871017089534737L;
	
	private QuickSet quickSet;
	
	private Job job;//关联岗位
	
	private String desc;//描述

	public QuickSet getQuickSet() {
		return quickSet;
	}

	public void setQuickSet(QuickSet quickSet) {
		this.quickSet = quickSet;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	

}
