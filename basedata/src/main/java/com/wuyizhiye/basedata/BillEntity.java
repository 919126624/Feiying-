package com.wuyizhiye.basedata;

import java.util.Date;

/**
 * @ClassName BillEntity
 * @Description 单据基类
 * @author li.biao
 * @date 2015-4-2
 */
public abstract class BillEntity extends DataEntity {
	private static final long serialVersionUID = 8846238709975238838L;
	
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

	public String getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(String processInstance) {
		this.processInstance = processInstance;
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
	
	public String getProcessName(){
		return null;
	}
}
