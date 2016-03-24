package com.wuyizhiye.hr.affair.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * 工作经历
 * @author Cai.xing
 * @since 2013-04-03
 */
public class WorkExperience extends CoreEntity{
	private static final long serialVersionUID = 1L;
	/**
	 * 人员
	 */
	private EmployeeOrientation employee;
	/**
	 * 开始时间
	 */
	private Date startDate;
	/**
	 * 截止时间
	 */
	private Date endDate;
	/**
	 * 工作单位
	 */
	private String workUnit;
	/**
	 * 工作岗位
	 */
	private String workPosition;
	/**
	 * 离职原因
	 */
	private String leaveReason;
	/**
	 * 证明人
	 */
	private String prover;
	/**
	 * 证明人电话
	 */
	private String proverTel;
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getWorkUnit() {
		return workUnit;
	}
	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}
	public String getWorkPosition() {
		return workPosition;
	}
	public void setWorkPosition(String workPosition) {
		this.workPosition = workPosition;
	}
	public String getLeaveReason() {
		return leaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	public String getProver() {
		return prover;
	}
	public void setProver(String prover) {
		this.prover = prover;
	}
	public String getProverTel() {
		return proverTel;
	}
	public void setProverTel(String proverTel) {
		this.proverTel = proverTel;
	}
	public EmployeeOrientation getEmployee() {
		return employee;
	}
	public void setEmployee(EmployeeOrientation employee) {
		this.employee = employee;
	}
}
