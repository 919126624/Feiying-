package com.wuyizhiye.hr.attendance.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
/**
 * 请假规则
 * @author hyl
 * @since 2013-11-18 
 */
public class LeaveRule extends CoreEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7900495962570908848L;
	/**
	 * 所属组织（公司）
	 */
	private Org org;
	/**
	 * 年假提前天数
	 */
	private Integer annualLeave;
	/**
	 * 事假提前天数
	 */
	private Integer compassionateLeave;
	/**
	 * 病假提前天数
	 */
	private Integer sickLeave;
	/**
	 * 婚假提前天数
	 */
	private Integer marriageLeave;
	/**
	 * 丧假提前天数
	 */
	private Integer bereavementLeave;
	/**
	 * 计生假提前天数
	 */
	private Integer ippfLeave;
	/**
	 * 其他假期提前天数
	 */
	private Integer ohter;
	/**
	 * 是否启用
	 */
	private Boolean enable;
	/**
	 * 规则名称
	 */
	private String name;
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 创建用户
	 */
	private Person creator;
	/**
	 * 创建时间
	 */
	private Date createTime;
	
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
	public Person getCreator() {
		return creator;
	}
	public void setCreator(Person creator) {
		this.creator = creator;
	}
	public Person getUpdator() {
		return updator;
	}
	public void setUpdator(Person updator) {
		this.updator = updator;
	}
	/**
	 * 最后更新用户
	 */
	private Person updator;
	/**
	 * 最后修改时间
	 */
	private Date lastUpdateTime;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Org getOrg() {
		return org;
	}
	public void setOrg(Org org) {
		this.org = org;
	}
	public Integer getAnnualLeave() {
		return annualLeave;
	}
	public void setAnnualLeave(Integer annualLeave) {
		this.annualLeave = annualLeave;
	}
	public Integer getCompassionateLeave() {
		return compassionateLeave;
	}
	public void setCompassionateLeave(Integer compassionateLeave) {
		this.compassionateLeave = compassionateLeave;
	}
	public Integer getSickLeave() {
		return sickLeave;
	}
	public void setSickLeave(Integer sickLeave) {
		this.sickLeave = sickLeave;
	}
	public Integer getMarriageLeave() {
		return marriageLeave;
	}
	public void setMarriageLeave(Integer marriageLeave) {
		this.marriageLeave = marriageLeave;
	}
	public Integer getBereavementLeave() {
		return bereavementLeave;
	}
	public void setBereavementLeave(Integer bereavementLeave) {
		this.bereavementLeave = bereavementLeave;
	}
	public Integer getIppfLeave() {
		return ippfLeave;
	}
	public void setIppfLeave(Integer ippfLeave) {
		this.ippfLeave = ippfLeave;
	}
	public Integer getOhter() {
		return ohter;
	}
	public void setOhter(Integer ohter) {
		this.ohter = ohter;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
