package com.wuyizhiye.cmct.sms.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.sms.enums.SMSControlTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSStrategyTypeEnum;

/**
 * @ClassName SMSControl
 * @Description 短信控制
 * @author li.biao
 * @date 2015-5-26
 */
public class SMSControl extends CoreEntity{
 
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
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	
	/**
	 * 最后更新用户
	 */
	private Person updator;
	
	/**
	 * 数据所属组织
	 */
	private Org org;
	
	private SMSControlTypeEnum  controlType; //控制类型  个人、部门、公司
	
	private String objectId ;//员工ID 或 组织ID 或  公司ID
	
	private String objectNumber ;//员工编码 或 组织编码 或  公司编码
	
	private String objectName ;//员工名称 或 组织名称 或  公司名称
	
	private Boolean balanceStrategyFlag ;//启用余额控制
	
	private int balanceAmout;  //余额条数
	
	private Boolean limitStrategyFlag ;//启用上限策略
	
	private SMSStrategyTypeEnum  limitStrategyType;//上限策略类型
	
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

	public SMSControlTypeEnum getControlType() {
		return controlType;
	}

	public void setControlType(SMSControlTypeEnum controlType) {
		this.controlType = controlType;
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

	public Boolean getBalanceStrategyFlag() {
		return balanceStrategyFlag;
	}

	public void setBalanceStrategyFlag(Boolean balanceStrategyFlag) {
		this.balanceStrategyFlag = balanceStrategyFlag;
	}

	public int getBalanceAmout() {
		return balanceAmout;
	}

	public void setBalanceAmout(int balanceAmout) {
		this.balanceAmout = balanceAmout;
	}

	public Boolean getLimitStrategyFlag() {
		return limitStrategyFlag;
	}

	public void setLimitStrategyFlag(Boolean limitStrategyFlag) {
		this.limitStrategyFlag = limitStrategyFlag;
	}

	public SMSStrategyTypeEnum getLimitStrategyType() {
		return limitStrategyType;
	}

	public void setLimitStrategyType(SMSStrategyTypeEnum limitStrategyType) {
		this.limitStrategyType = limitStrategyType;
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
