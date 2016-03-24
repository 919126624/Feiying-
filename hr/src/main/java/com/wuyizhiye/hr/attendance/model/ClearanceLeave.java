package com.wuyizhiye.hr.attendance.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.workflow.enums.BillStatusEnum;

/**
 * 销假
 * @author hyl
 * @since 2013-11-18 
 */
public class ClearanceLeave extends CoreEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1608815027326507150L;
	/**
	 * 请假实体
	 */
	private Leave leave;
	/**
	 * 实际开始时间
	 */
	private Date actualStartTime;
	/**
	 * 实际结束时间
	 */
	private Date actualEndTime;
	/**
	 * 实际请假天数
	 */
	private double actualLeaveDays;
	/**
	 * 销假理由
	 */
	private String  reasons4Cleareance;
	
	/**
	 * 销假状态
	 */
	private BillStatusEnum  leaveClearanceStatus;
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

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
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

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public Leave getLeave() {
		return leave;
	}

	public void setLeave(Leave leave) {
		this.leave = leave;
	}

	public Date getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(Date actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public Date getActualEndTime() {
		return actualEndTime;
	}

	public void setActualEndTime(Date actualEndTime) {
		this.actualEndTime = actualEndTime;
	}

	public double getActualLeaveDays() {
		return actualLeaveDays;
	}

	public void setActualLeaveDays(double actualLeaveDays) {
		this.actualLeaveDays = actualLeaveDays;
	}

	public String getReasons4Cleareance() {
		return reasons4Cleareance;
	}

	public void setReasons4Cleareance(String reasons4Cleareance) {
		this.reasons4Cleareance = reasons4Cleareance;
	}

	public BillStatusEnum getLeaveClearanceStatus() {
		return leaveClearanceStatus;
	}

	public void setLeaveClearanceStatus(
			BillStatusEnum leaveClearanceStatus) {
		this.leaveClearanceStatus = leaveClearanceStatus;
	}	
	
}
