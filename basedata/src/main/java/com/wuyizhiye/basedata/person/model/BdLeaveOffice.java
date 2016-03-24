package com.wuyizhiye.basedata.person.model;

import java.util.Date;

import com.wuyizhiye.basedata.basic.model.BasicData;

/**
 * @ClassName BdLeaveOffice
 * @Description 人员离职
 * @author li.biao
 * @date 2015-4-3
 */
public class BdLeaveOffice extends BdBillBase{
	private static final long serialVersionUID = 1L;
	/**
	 * 单据状态
	 * */
	private String billStatus;
	/**
	 * 异动类型
	 * */
	private String changeType;
	/**
	 * 备注
	 * */
	private String remark;
	/**
	 * 生效日期
	 * */
	private Date validateTime;
	
	/**
	 * 交接日期
	 * */
	private Date giveTime;
	
	/**
	 * 交接人
	 * */
	
	private Person givePerson;
	

	/**
	 * 离职原因
	 * */
	private BasicData basicData;
	/**
	 * 单据描述（用于审批流)
	 * */
	private String title;
	
	
	/**
	 * 创建人姓名
	 * */
	private String createName;
	/**
	 * 创建组织名
	 * */
	private String createOrgName;
	/**
	 * 创建人职位
	 * */
	private String createPositionName;
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getCreateOrgName() {
		return createOrgName;
	}

	public void setCreateOrgName(String createOrgName) {
		this.createOrgName = createOrgName;
	}

	public String getCreatePositionName() {
		return createPositionName;
	}

	public void setCreatePositionName(String createPositionName) {
		this.createPositionName = createPositionName;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public BasicData getBasicData() {
		return basicData;
	}
	public void setBasicData(BasicData basicData) {
		this.basicData = basicData;
	}
	public Date getGiveTime() {
		return giveTime;
	}
	public void setGiveTime(Date giveTime) {
		this.giveTime = giveTime;
	}
	public Person getGivePerson() {
		return givePerson;
	}
	public void setGivePerson(Person givePerson) {
		this.givePerson = givePerson;
	}
	public Date getValidateTime() {
		return validateTime;
	}
	public void setValidateTime(Date validateTime) {
		this.validateTime = validateTime;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
