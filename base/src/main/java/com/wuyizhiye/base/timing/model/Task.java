package com.wuyizhiye.base.timing.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.timing.TaskStatusEnum;
import com.wuyizhiye.base.timing.TaskTimeEnum;
import com.wuyizhiye.base.timing.TaskTypeEnum;

/**
 * @ClassName Task
 * @Description 定时任务,任务期限只限制到天,定点任务需要自行
 * @author li.biao
 * @date 2015-4-1
 */
public class Task extends CoreEntity {
	private static final long serialVersionUID = 90635854098497730L;

	/**
	 * 任务名称
	 */
	private String name;
	
	/**
	 * 任务说明
	 */
	private String description;
	
	/**
	 * 任务接口
	 */
	private String target;
	
	/**
	 * 任务参数,保存为value1;value2……格式
	 */
	private String params;
	
	/**
	 * 任务类型
	 */
	private TaskTypeEnum type;
	
	/**
	 * 执行时间
	 */
	private TaskTimeEnum time;
	
	/**
	 * 任务状态,默认待执行
	 */
	private TaskStatusEnum status = TaskStatusEnum.WAIT;
	
	/**
	 * 执行次数
	 */
	private Integer times = 0;
	
	/**
	 * 任务创建时间
	 */
	private Date createTime;
	
	/**
	 * 生效日期
	 */
	private Date effectDate;
	
	/**
	 * 最后执行时间
	 */
	private Date lastRunTime;
	
	/**
	 * 执行耗时
	 */
	private Long timeConsuming;
	
	/**
	 * 最后执行信息
	 */
	private String lastRunMsg;
	
	/**
	 * 数据中心
	 */
	private String dataCenter;
	/**
	 * 禁用 
	 */
	private int isValid;
	/**
	 * 运行时间
	 */
	private Date runTime;
	
    private String syncType;//数据同步类型 CLOUD为云端数据
	
	private Date syncDate;//数据同步日期
	
	private String cloudId;//对应云上数据ID
	
	public Date getRunTime() {
		return runTime;
	}

	public void setRunTime(Date runTime) {
		this.runTime = runTime;
	}

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public TaskTypeEnum getType() {
		return type;
	}

	public void setType(TaskTypeEnum type) {
		this.type = type;
	}

	public TaskTimeEnum getTime() {
		return time;
	}

	public void setTime(TaskTimeEnum time) {
		this.time = time;
	}

	public TaskStatusEnum getStatus() {
		return status;
	}

	public void setStatus(TaskStatusEnum status) {
		this.status = status;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(Date lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

	public Long getTimeConsuming() {
		return timeConsuming;
	}

	public void setTimeConsuming(Long timeConsuming) {
		this.timeConsuming = timeConsuming;
	}

	public String getLastRunMsg() {
		return lastRunMsg;
	}

	public void setLastRunMsg(String lastRunMsg) {
		this.lastRunMsg = lastRunMsg;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	public String getDataCenter() {
		return dataCenter;
	}

	public void setDataCenter(String dataCenter) {
		this.dataCenter = dataCenter;
	}

	public String getSyncType() {
		return syncType;
	}

	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}

	public Date getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(Date syncDate) {
		this.syncDate = syncDate;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}
	
}
