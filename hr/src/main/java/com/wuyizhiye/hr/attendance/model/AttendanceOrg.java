package com.wuyizhiye.hr.attendance.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * 考勤组织方案 
 * @author ouyangyi
 * @since 2013-11-19 上午10:26:38
 */
public class AttendanceOrg extends CoreEntity{
 
	private static final long serialVersionUID = -6217293970469179137L;

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 创建用户
	 */
	private Person creator;//考勤录入人
	
	
	private String orgId ;//考勤组织
	
	private String orgName ;//考勤组织名称

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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
}
