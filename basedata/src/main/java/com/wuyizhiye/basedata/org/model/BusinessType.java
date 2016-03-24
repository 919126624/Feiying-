package com.wuyizhiye.basedata.org.model;

import java.util.Date;

import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName BusinessType
 * @Description 业务类型
 * @author li.biao
 * @date 2015-4-2
 */
public class BusinessType extends DataEntity {
	private static final long serialVersionUID = 2308319074029792785L;
	/**
	 * 启用
	 */
	private Boolean enable;
	
	/**
	 * 行业
	 */
	private BusinessTypeEnum type;
	
    private String syncType;//数据同步类型 CLOUD为云端数据
	
	private Date syncDate;//数据同步日期
	
	private String cloudId;//对应云上数据ID

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public BusinessTypeEnum getType() {
		return type;
	}

	public void setType(BusinessTypeEnum type) {
		this.type = type;
	}

	public String getSyncType() {
		return syncType;
	}

	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}

	public Date getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(Date syncDate) {
		this.syncDate = syncDate;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}
	
	
}
