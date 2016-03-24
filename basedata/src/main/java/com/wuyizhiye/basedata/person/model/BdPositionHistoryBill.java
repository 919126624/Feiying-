package com.wuyizhiye.basedata.person.model;

import java.util.Date;

import com.wuyizhiye.basedata.basic.model.BasicData;

/**
 * @ClassName BdPositionHistoryBill
 * @Description 员工（组织、岗位、职级）异动单据model
 * @author li.biao
 * @date 2015-4-3
 */
public class BdPositionHistoryBill extends BdBillBase{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2195970507226289235L;

    private String billNumber;//单据编号

    private BasicData jobStatus;//异动员工 岗位状态
    
    private String changeType;//异动类型
    
    private String changeTypeName;//异动类型 名称

    private Boolean primary;//是否为主要岗位
    
    private Date takeOfficeDate;//任职日期
    
    private String billStatus;//单据状态
    
    private String billStatusName;//单据状态名称
    
    private String remark;//备注

    private String isdisable;//是否有效
    
    private Date effectdate;//生效日期
    
    private Person handOverPerson ;//交接人
    
    private Date handOverDate;//交接日期
    
    private String title;//单据描述（用于审批流)

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public BasicData getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(BasicData jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getChangeTypeName() {
		return changeTypeName;
	}

	public void setChangeTypeName(String changeTypeName) {
		this.changeTypeName = changeTypeName;
	}

	public Boolean getPrimary() {
		return primary;
	}

	public void setPrimary(Boolean primary) {
		this.primary = primary;
	}

	public Date getTakeOfficeDate() {
		return takeOfficeDate;
	}

	public void setTakeOfficeDate(Date takeOfficeDate) {
		this.takeOfficeDate = takeOfficeDate;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getBillStatusName() {
		return billStatusName;
	}

	public void setBillStatusName(String billStatusName) {
		this.billStatusName = billStatusName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsdisable() {
		return isdisable;
	}

	public void setIsdisable(String isdisable) {
		this.isdisable = isdisable;
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

	public Person getHandOverPerson() {
		return handOverPerson;
	}

	public void setHandOverPerson(Person handOverPerson) {
		this.handOverPerson = handOverPerson;
	}

	public Date getHandOverDate() {
		return handOverDate;
	}

	public void setHandOverDate(Date handOverDate) {
		this.handOverDate = handOverDate;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "@" + Integer.toHexString(hashCode());
	}
}