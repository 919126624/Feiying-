package com.wuyizhiye.hr.affair.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.hr.enums.BillEnum;

/**
 * 单据信息(公司动态)
 * @author hlz
 *
 */
public class ProcessInfo extends CoreEntity {
	private static final long serialVersionUID = 7950905616077388683L;
	/**
	 * 审批日期
	 */
	private Date auditDate;
	/**
	 * 单据类型（大类型）
	 */
	private BillEnum billType;
	/**
	 * 申请人
	 */
	private Person apply;
	/**
	 * 异动前所在组织
	 */
	private Org oldOrg;
	/**
	 * 异动前职位
	 */
	private Position oldPosition;
	/**
	 * 异动前职级
	 */
	private JobLevel oldJobLevel;
	/**
	 * 变动后组织
	 */
	private Org changeOrg;
	/**
	 * 变动后职位
	 */
	private Position changePosition;
	/**
	 * 变动后职级
	 */
	private JobLevel changeJobLevel;
	
	/**
	 * 离职交接人
	 */
	private Person givePerson;
	
	public Person getGivePerson() {
		return givePerson;
	}
	public void setGivePerson(Person givePerson) {
		this.givePerson = givePerson;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public BillEnum getBillType() {
		return billType;
	}
	public void setBillType(BillEnum billType) {
		this.billType = billType;
	}
	public Person getApply() {
		return apply;
	}
	public void setApply(Person apply) {
		this.apply = apply;
	}
	public Org getOldOrg() {
		return oldOrg;
	}
	public void setOldOrg(Org oldOrg) {
		this.oldOrg = oldOrg;
	}
	public Position getOldPosition() {
		return oldPosition;
	}
	public void setOldPosition(Position oldPosition) {
		this.oldPosition = oldPosition;
	}
	public JobLevel getOldJobLevel() {
		return oldJobLevel;
	}
	public void setOldJobLevel(JobLevel oldJobLevel) {
		this.oldJobLevel = oldJobLevel;
	}
	public Org getChangeOrg() {
		return changeOrg;
	}
	public void setChangeOrg(Org changeOrg) {
		this.changeOrg = changeOrg;
	}
	public Position getChangePosition() {
		return changePosition;
	}
	public void setChangePosition(Position changePosition) {
		this.changePosition = changePosition;
	}
	public JobLevel getChangeJobLevel() {
		return changeJobLevel;
	}
	public void setChangeJobLevel(JobLevel changeJobLevel) {
		this.changeJobLevel = changeJobLevel;
	}
	
}
