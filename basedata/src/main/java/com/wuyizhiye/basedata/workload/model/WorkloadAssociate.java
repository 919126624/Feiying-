package com.wuyizhiye.basedata.workload.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName WorkloadAssociate
 * @Description 关联工作量和岗位
 * @author li.biao
 * @date 2015-4-3
 */
public class WorkloadAssociate extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	
	private String jobId;//人员
	private String workloadId;//可用快捷菜单
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getShoutcutId() {
		return workloadId;
	}
	public void setShoutcutId(String workloadId) {
		this.workloadId = workloadId;
	}

	
}
