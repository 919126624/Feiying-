package com.wuyizhiye.cmct.sms.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.sms.enums.SMSOperationTypeEnum;

/**
 * @ClassName SMSControlHistory
 * @Description  短信控制策略历史
 * @author li.biao
 * @date 2015-5-26
 */
public class SMSControlHistory extends CoreEntity{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 6871331191896848902L;

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 创建用户
	 */
	private Person creator;
	
	/**
	 * 数据所属组织
	 */
	private Org org;
	
	private String controlId; //短信控制ID
	
	private SMSOperationTypeEnum operationtype;//操作类型   改策略、充值、扣减
	
	private String objectId ;//员工ID 或 组织ID 或  公司ID
	
	private String objectNumber ;//员工编码 或 组织编码 或  公司编码
	
	private String objectName ;//员工名称 或 组织名称 或  公司名称
	
	private int balanceAmout;  //余额条数
	
	private int topLimitAmount ;   //上限条数
	
	private String description;     //操作描述

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

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public String getControlId() {
		return controlId;
	}

	public void setControlId(String controlId) {
		this.controlId = controlId;
	}

	public SMSOperationTypeEnum getOperationtype() {
		return operationtype;
	}

	public void setOperationtype(SMSOperationTypeEnum operationtype) {
		this.operationtype = operationtype;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectNumber() {
		return objectNumber;
	}

	public void setObjectNumber(String objectNumber) {
		this.objectNumber = objectNumber;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public int getBalanceAmout() {
		return balanceAmout;
	}

	public void setBalanceAmout(int balanceAmout) {
		this.balanceAmout = balanceAmout;
	}

	public int getTopLimitAmount() {
		return topLimitAmount;
	}

	public void setTopLimitAmount(int topLimitAmount) {
		this.topLimitAmount = topLimitAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
