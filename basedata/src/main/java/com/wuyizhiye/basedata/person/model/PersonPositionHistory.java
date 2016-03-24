package com.wuyizhiye.basedata.person.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.basic.model.BasicData;

/**
 * @ClassName PersonPositionHistory
 * @Description 人员(组织、岗位、职级)异动表
 * @author li.biao
 * @date 2015-4-3
 */
public class PersonPositionHistory extends CoreEntity {
	public static final String Mapper = "com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao";
    /**
	 * 
	 */
	private static final long serialVersionUID = 1243687802601573051L;
	
	/**
	 * 交接人 added by hlz 
	 * 用于冗余显示
	 */
	private String givePersonId;

	private String personId;//异动员工ID
    
    private String personNumber;//异动员工编号
    
    private String personName;//异动员工名称

    private BasicData jobStatus;//异动人员  岗位状态

    private String changeType;//异动类型

    private String oldOrgId;//异动前 组织ID
    
    private String oldOrgName;//异动前组织名称

    private String oldPositionId;//异动前 岗位ID
    
    private String oldPositionName;//异动前 岗位名称

    private String oldJobLevel;//异动前 职级ID
    
    private String oldJobLevelName;//异动前职级名称

    private String changeOrgId;//异动后 组织ID
    
    private String changeOrgName;//异动后 组织名称

    private String changePositionId;//异动后 岗位ID
    
    private String changePositionName;//异动后 岗位名称

    private String changeJobLevel;//异动后职级ID
    
    private String changeJobLevelName;//异动后职级ID

    private boolean primary;

    private Date effectdate;

    private Date expirydate;

    private String creatorId;//异动单 录入人
    
    private String creatorName;//异动单 录入人名称

    private String orgId;//异动单 录入人组织
    
    private String orgIdName;//异动单 录入人组织名称

    private Date createTime;

    private String updator;//异动单 修改人名称
    
    private String updatorName;//异动单 修改人名称

    private Date lastupdateTime;

    private String isdisable;
    
    private String oldPositionJobId;	//冗余字段 岗位id

	public String getOldPositionJobId() {
		return oldPositionJobId;
	}

	public void setOldPositionJobId(String oldPositionJobId) {
		this.oldPositionJobId = oldPositionJobId;
	}

	public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId == null ? null : personId.trim();
    }

    public BasicData getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(BasicData jobStatus) {
        this.jobStatus = jobStatus ;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType == null ? null : changeType.trim();
    }

    public String getOldOrgId() {
        return oldOrgId;
    }

    public void setOldOrgId(String oldOrgId) {
        this.oldOrgId = oldOrgId == null ? null : oldOrgId.trim();
    }

    public String getOldPositionId() {
        return oldPositionId;
    }

    public void setOldPositionId(String oldPositionId) {
        this.oldPositionId = oldPositionId == null ? null : oldPositionId.trim();
    }

    public String getOldJobLevel() {
        return oldJobLevel;
    }

    public void setOldJobLevel(String oldJobLevel) {
        this.oldJobLevel = oldJobLevel == null ? null : oldJobLevel.trim();
    }

    public String getChangeOrgId() {
        return changeOrgId;
    }

    public void setChangeOrgId(String changeOrgId) {
        this.changeOrgId = changeOrgId == null ? null : changeOrgId.trim();
    }

    public String getChangePositionId() {
        return changePositionId;
    }

    public void setChangePositionId(String changePositionId) {
        this.changePositionId = changePositionId == null ? null : changePositionId.trim();
    }

    public String getChangeJobLevel() {
        return changeJobLevel;
    }

    public void setChangeJobLevel(String changeJobLevel) {
        this.changeJobLevel = changeJobLevel == null ? null : changeJobLevel.trim();
    }

    public boolean getPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public Date getEffectdate() {
        return effectdate;
    }

    public void setEffectdate(Date effectdate) {
        this.effectdate = effectdate;
    }

    public Date getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(Date expirydate) {
        this.expirydate = expirydate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId == null ? null : creatorId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public Date getLastupdateTime() {
        return lastupdateTime;
    }

    public void setLastupdateTime(Date lastupdateTime) {
        this.lastupdateTime = lastupdateTime;
    }

    public String getIsdisable() {
        return isdisable;
    }

    public void setIsdisable(String isdisable) {
        this.isdisable = isdisable == null ? null : isdisable.trim();
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

	public String getOldOrgName() {
		return oldOrgName;
	}

	public void setOldOrgName(String oldOrgName) {
		this.oldOrgName = oldOrgName;
	}

	public String getOldPositionName() {
		return oldPositionName;
	}

	public void setOldPositionName(String oldPositionName) {
		this.oldPositionName = oldPositionName;
	}

	public String getOldJobLevelName() {
		return oldJobLevelName;
	}

	public void setOldJobLevelName(String oldJobLevelName) {
		this.oldJobLevelName = oldJobLevelName;
	}

	public String getChangeOrgName() {
		return changeOrgName;
	}

	public void setChangeOrgName(String changeOrgName) {
		this.changeOrgName = changeOrgName;
	}

	public String getChangePositionName() {
		return changePositionName;
	}

	public void setChangePositionName(String changePositionName) {
		this.changePositionName = changePositionName;
	}

	public String getChangeJobLevelName() {
		return changeJobLevelName;
	}

	public void setChangeJobLevelName(String changeJobLevelName) {
		this.changeJobLevelName = changeJobLevelName;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getOrgIdName() {
		return orgIdName;
	}

	public void setOrgIdName(String orgIdName) {
		this.orgIdName = orgIdName;
	}

	public String getUpdatorName() {
		return updatorName;
	}

	public void setUpdatorName(String updatorName) {
		this.updatorName = updatorName;
	}

	public String getGivePersonId() {
		return givePersonId;
	}

	public void setGivePersonId(String givePersonId) {
		this.givePersonId = givePersonId;
	}
    
}