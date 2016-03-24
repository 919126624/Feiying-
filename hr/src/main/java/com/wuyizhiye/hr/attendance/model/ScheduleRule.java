package com.wuyizhiye.hr.attendance.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
/**
 * 排班设置
 * @author hyl
 * @since 2013-11-25 
 */
public class ScheduleRule extends CoreEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3292209289320614063L;

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
	 * 规则所属组织（公司）
	 */
	private Org org;
	/**
	 * 早班开始时间
	 */
	private String  morning_StartTime;
	/**
	 *早班结束时间
	 */
	private String   morning_EndTime;
	/**
	 * 晚班开始时间
	 */
	private String  night_StartTime;
	/**
	 *晚班结束时间
	 */
	private String  night_EndTime;
	/**
	 * 是否启用
	 */
	private Boolean enable;
	
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
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
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
	public Org getOrg() {
		return org;
	}
	public void setOrg(Org org) {
		this.org = org;
	}
	public String getMorning_StartTime() {
		return morning_StartTime;
	}
	public void setMorning_StartTime(String morning_StartTime) {
		this.morning_StartTime = morning_StartTime;
	}
	public String getMorning_EndTime() {
		return morning_EndTime;
	}
	public void setMorning_EndTime(String morning_EndTime) {
		this.morning_EndTime = morning_EndTime;
	}
	public String getNight_StartTime() {
		return night_StartTime;
	}
	public void setNight_StartTime(String night_StartTime) {
		this.night_StartTime = night_StartTime;
	}
	public String getNight_EndTime() {
		return night_EndTime;
	}
	public void setNight_EndTime(String night_EndTime) {
		this.night_EndTime = night_EndTime;
	}
	
}
