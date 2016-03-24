package com.wuyizhiye.hr.attendance.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.workflow.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.Ask4LeaveTypeEnum;

/**
 * 请假
 * @author hyl
 * @since 2013-11-18 
 */
public class Leave extends CoreEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3559879109392040963L;
	/**
	 * 申请人
	 */
	private Person applyPerson;
	/**
	 * 请假类型
	 */
	private Ask4LeaveTypeEnum ask4LeaveType;
	/**
	 * 请假开始时间
	 */
	private Date ask4LeaveStartTime;
	
	/**
	 * 请假结束时间
	 */
	private Date ask4LeaveEndTime;
	/**
	 * 请假天数
	 */
	private double leaveDays;
	/**
	 * 请假事由
	 */
	private String  reasons4Leave;
	/**
	 * 请假状态
	 */
	private BillStatusEnum  ask4LeaveStatus;
	/**
	 * 申请人岗位（冗余字段）
	 */
	private Position personPosition;
	/**
	 * 提交时间
	 */
	private Date submitDate;
	
	/**
	 * 审批完成时间
	 */
	private Date auditDate;
	
	/**
	 * 流程实例id
	 */
	private String processInstance;
	/**
	 * 单据编号
	 */
    private String billNumber;
    /**
	 * 异动类型
	 */
	private String changeType;
	/**
	 * 生效日期
	 */
	private Date effectdate;
	/**
	 * 单据描述（用于审批流)
	 */
	private String title;
	/**
	 * 创建用户
	 */
	private Person creator;
	/**
	 * 销假实例
	 */
	private ClearanceLeave cLeave;
	/**
	 * 最后更新用户
	 */
	private Person updator;
	/**
	 * 最后修改时间
	 */
	private Date lastUpdateTime;

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
	public Person getUpdator() {
		return updator;
	}
	public void setUpdator(Person updator) {
		this.updator = updator;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public ClearanceLeave getcLeave() {
		return cLeave;
	}
	public void setcLeave(ClearanceLeave cLeave) {
		this.cLeave = cLeave;
	}
	public Person getCreator() {
		return creator;
	}
	public void setCreator(Person creator) {
		this.creator = creator;
	}
	
	
	
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public Date getEffectdate() {
		return effectdate;
	}
	public void setEffectdate(Date effectdate) {
		this.effectdate = effectdate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getProcessInstance() {
		return processInstance;
	}
	public void setProcessInstance(String processInstance) {
		this.processInstance = processInstance;
	}
	
	public Position getPersonPosition() {
		return personPosition;
	}
	public void setPersonPosition(Position personPosition) {
		this.personPosition = personPosition;
	}
	public Person getApplyPerson() {
		return applyPerson;
	}
	public void setApplyPerson(Person applyPerson) {
		this.applyPerson = applyPerson;
	}
	public Ask4LeaveTypeEnum getAsk4LeaveType() {
		return ask4LeaveType;
	}
	public void setAsk4LeaveType(Ask4LeaveTypeEnum ask4LeaveType) {
		this.ask4LeaveType = ask4LeaveType;
	}
	public Date getAsk4LeaveStartTime() {
		return ask4LeaveStartTime;
	}
	public void setAsk4LeaveStartTime(Date ask4LeaveStartTime) {
		this.ask4LeaveStartTime = ask4LeaveStartTime;
	}
	public Date getAsk4LeaveEndTime() {
		return ask4LeaveEndTime;
	}
	public void setAsk4LeaveEndTime(Date ask4LeaveEndTime) {
		this.ask4LeaveEndTime = ask4LeaveEndTime;
	}
	public double getLeaveDays() {
		return leaveDays;
	}
	public void setLeaveDays(double leaveDays) {
		this.leaveDays = leaveDays;
	}
	public String getReasons4Leave() {
		return reasons4Leave;
	}
	public void setReasons4Leave(String reasons4Leave) {
		this.reasons4Leave = reasons4Leave;
	}
	public BillStatusEnum getAsk4LeaveStatus() {
		return ask4LeaveStatus;
	}
	public void setAsk4LeaveStatus(BillStatusEnum ask4LeaveStatus) {
		this.ask4LeaveStatus = ask4LeaveStatus;
	}
	
}
