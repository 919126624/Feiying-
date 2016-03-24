package com.wuyizhiye.basedata.org.model;

import java.util.List;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.org.enums.WorkBenchTypeEnum;

/**
 * @ClassName Job
 * @Description 岗位 
 * @author li.biao
 * @date 2015-4-2
 */
public class Job extends DataEntity {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 职等
	 */
	private List<JobLevel> jobLevel;
	
	/**
	 * 工作台类型
	 */
	private WorkBenchTypeEnum workBenchType;
	
	/**
	 * 岗位大类
	 */
	private JobCategory jobCategory;

	private Integer  hasLevel;
	
	/**
	 * 添加微信组织字段
	 */
	private WeixinOrg weixinOrg;
	
	/**
	 * 职等
	 */
	private JobEtc jobEtc;
	
	public Job(){}
	public Job(String id){
		super.setId(id);
	}
	
	public JobEtc getJobEtc() {
		return jobEtc;
	}

	public void setJobEtc(JobEtc jobEtc) {
		this.jobEtc = jobEtc;
	}

	public WeixinOrg getWeixinOrg() {
		return weixinOrg;
	}

	public void setWeixinOrg(WeixinOrg weixinOrg) {
		this.weixinOrg = weixinOrg;
	}

	public JobCategory getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(JobCategory jobCategory) {
		this.jobCategory = jobCategory;
	}

	public void setJobLevel(List<JobLevel> jobLevel) {
		this.jobLevel = jobLevel;
	}

	public List<JobLevel> getJobLevel() {
		return jobLevel;
	}

	public void setWorkBenchType(WorkBenchTypeEnum workBenchType) {
		this.workBenchType = workBenchType;
	}

	public WorkBenchTypeEnum getWorkBenchType() {
		return workBenchType;
	}

	public Integer getHasLevel() {
		return hasLevel;
	}

	public void setHasLevel(Integer hasLevel) {
		this.hasLevel = hasLevel;
	}
}
