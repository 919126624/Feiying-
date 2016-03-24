package com.wuyizhiye.hr.salary.model;

import java.util.Date;
import java.util.List;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * 薪酬标准
 * @author hyl
 * @since 2014-02-12
 *
 */
public class SalaryStandard extends CoreEntity{
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 创建用户
	 */
	private Person creator;
	
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	
	/**
	 * 最后更新用户
	 */
	private Person updator;
	/**
	 * 岗位
	 */
	private Job job;
	
	/**
	 * 职级
	 */
	private JobLevel jobLevel;
	/**
	 * 非表中字段
	 * @return
	 */
	private List<StandardItem> standardItemList;//薪酬项目
	@SuppressWarnings("unused")
	private int minLength;//编辑时用 
	
	private String detailJson;//表单提交时用

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Person getUpdator() {
		return updator;
	}

	public void setUpdator(Person updator) {
		this.updator = updator;
	}
	
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public JobLevel getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(JobLevel jobLevel) {
		this.jobLevel = jobLevel;
	}

	public List<StandardItem> getStandardItemList() {
		return standardItemList;
	}

	public void setStandardItemList(List<StandardItem> standardItemList) {
		this.standardItemList = standardItemList;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public String getDetailJson() {
		return detailJson;
	}

	public void setDetailJson(String detailJson) {
		this.detailJson = detailJson;
	}
	
}
