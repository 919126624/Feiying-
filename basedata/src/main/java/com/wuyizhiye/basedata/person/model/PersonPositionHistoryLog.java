package com.wuyizhiye.basedata.person.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.enums.OperateTypeEnum;

/**
 * @ClassName PersonPositionHistoryLog
 * @Description 人员(组织、岗位、职级)调整日志 
 * @author li.biao
 * @date 2015-4-3
 */
public class PersonPositionHistoryLog extends CoreEntity {
 
	private static final long serialVersionUID = 4534728721010429790L;

	public static final String Mapper = "com.wuyizhiye.basedata.person.dao.PersonPositionHistoryLogDao";

	private String personId;//任职历史员工ID
    
    private String personNumber;//任职历史员工编号
    
    private String personName;//任职历史员工名称

    private String personOrgId;//任职历史人组织
    
    private String personOrgIdName;//任职历史人组织名称
    
    private OperateTypeEnum operateType;//操作类型
    
    private String description;//任职历史调整明细描述
    
    private String descriptionId;//任职历史调整明细描述  以对象ID方式保存

    private String creatorId;//操作人
    
    private String creatorName;//操作人名称

    private String orgId;//操作人组织
    
    private String orgIdName;//操作人组织名称

    private Date createTime;//操作时间

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

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonNumber() {
		return personNumber;
	}

	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonOrgId() {
		return personOrgId;
	}

	public void setPersonOrgId(String personOrgId) {
		this.personOrgId = personOrgId;
	}

	public String getPersonOrgIdName() {
		return personOrgIdName;
	}

	public void setPersonOrgIdName(String personOrgIdName) {
		this.personOrgIdName = personOrgIdName;
	}

	public OperateTypeEnum getOperateType() {
		return operateType;
	}

	public void setOperateType(OperateTypeEnum operateType) {
		this.operateType = operateType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionId() {
		return descriptionId;
	}

	public void setDescriptionId(String descriptionId) {
		this.descriptionId = descriptionId;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgIdName() {
		return orgIdName;
	}

	public void setOrgIdName(String orgIdName) {
		this.orgIdName = orgIdName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}