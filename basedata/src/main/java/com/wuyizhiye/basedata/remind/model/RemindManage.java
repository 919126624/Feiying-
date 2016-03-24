package com.wuyizhiye.basedata.remind.model;

import java.util.List;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.JobLevel;

/**
 * @ClassName RemindManage
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class RemindManage extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	
	private JobLevel jobLevel;//职级
	private Remind remind;//提醒项目
	private Job job;//不对应数据库
	private List<Remind> remindList;//不对应数据库
	public JobLevel getJobLevel() {
		return jobLevel;
	}
	public void setJobLevel(JobLevel jobLevel) {
		this.jobLevel = jobLevel;
	}
	public Remind getRemind() {
		return remind;
	}
	public void setRemind(Remind remind) {
		this.remind = remind;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public List<Remind> getRemindList() {
		return remindList;
	}
	public void setRemindList(List<Remind> remindList) {
		this.remindList = remindList;
	}
	
}
