package com.wuyizhiye.base.timing.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName TaskLog
 * @Description 任务日志
 * @author li.biao
 * @date 2015-4-1
 */
public class TaskLog extends CoreEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 创建用户
	 */
	private String creatorName;
	
	/**
	 * 信息
	 */
	private String info;
	
	/**
	 * 手动执行用户id(非手动无ID)
	 */
	private String personId;
	
	/**
	 * Task
	 */
	private Task task;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
}
