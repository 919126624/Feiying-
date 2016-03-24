package com.wuyizhiye.hr.attendance.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.workflow.enums.BillStatusEnum;

/**
 * 加班申请
 * @author hyl
 * @since 2013-11-28 
 */
public class OverTime extends CoreEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 985715473377532462L;
	/**
	 * 申请人
	 */
	private Person applyPerson;
	/**
	 * 加班日期
	 */
	private Date overTimeDate;
	/**
	 * 加班开始时间
	 */
	private String overTime_StartTime;
	
	/**
	 * 加班结束时间
	 */
	private String overTime_EndTime;
	/**
	 * 加班时长合计
	 */
	private double timeTotal;
	/**
	 *工作内容
	 */
	private String  workContent;
	/**
	 * 单据状态
	 */
	private BillStatusEnum  billStatus;
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
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	
	/**
	 * 最后更新用户
	 */
	private Person updator;
	
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
	public Person getApplyPerson() {
		return applyPerson;
	}
	public void setApplyPerson(Person applyPerson) {
		this.applyPerson = applyPerson;
	}
	public Date getOverTimeDate() {
		return overTimeDate;
	}
	public void setOverTimeDate(Date overTimeDate) {
		this.overTimeDate = overTimeDate;
	}
	public String getOverTime_StartTime() {
		return overTime_StartTime;
	}
	public void setOverTime_StartTime(String overTime_StartTime) {
		this.overTime_StartTime = overTime_StartTime;
	}
	public String getOverTime_EndTime() {
		return overTime_EndTime;
	}
	public void setOverTime_EndTime(String overTime_EndTime) {
		this.overTime_EndTime = overTime_EndTime;
	}
	public double getTimeTotal() {
		return timeTotal;
	}
	public void setTimeTotal(double timeTotal) {
		this.timeTotal = timeTotal;
	}
	public String getWorkContent() {
		return workContent;
	}
	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}
	public BillStatusEnum getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(BillStatusEnum billStatus) {
		this.billStatus = billStatus;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Position getPersonPosition() {
		return personPosition;
	}
	public void setPersonPosition(Position personPosition) {
		this.personPosition = personPosition;
	}
	
}
