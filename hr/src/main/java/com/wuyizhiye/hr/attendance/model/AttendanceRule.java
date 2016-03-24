package com.wuyizhiye.hr.attendance.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.hr.enums.AttendanceRuleTypeEnum;

/**
 * 考勤规则
 * @author ouyangyi
 * @since 2013-11-19 上午10:26:38
 */
public class AttendanceRule extends CoreEntity{
	
	public static final String Mapper = "com.wuyizhiye.hr.attendance.dao.AttendanceRuleDao";
	
	private static final long serialVersionUID = -2364342559879323428L;

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
	
	private AttendanceRuleTypeEnum ruleType;//规则类型（五天制、六天制、七天制)
	
	private Org org ;//规则所属组织
	
	private Position position ;// 职位
	
	private Job job;//岗位
	
	private int hour;
	
	private int minute;
	
	
	private String reminderTime ;//提醒时间
	
	/**
	 * 数据所属CU
	 */
	private Org controlUnit;
	

	public Org getControlUnit() {
		return controlUnit;
	}

	public void setControlUnit(Org controlUnit) {
		this.controlUnit = controlUnit;
	}

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

	public AttendanceRuleTypeEnum getRuleType() {
		return ruleType;
	}

	public void setRuleType(AttendanceRuleTypeEnum ruleType) {
		this.ruleType = ruleType;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public String getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(String reminderTime) {
		this.reminderTime = reminderTime;
	}
	
}
