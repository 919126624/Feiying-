package com.wuyizhiye.basedata.apiCenter.model;

import java.util.Date;

import com.wuyizhiye.base.common.enums.DataTypeEnum;
import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName APIParameter
 * @Description 接口参数
 * @author li.biao
 * @date 2015-4-2
 */
public class APIParameter extends DataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 接口
	 */
	private APIItem item;

	/**
	 * 参数序号
	 */
	private int idx;

	/**
	 * 参数类型
	 */
	private DataTypeEnum type;

	/**
	 * 参数说明
	 */
	private String description;
	
	/**
	 * 是否必填
	 */
	private int isNotNull;
	
    private String syncType;//数据同步类型 CLOUD为云端数据
	
	private Date syncDate;//数据同步日期
	
	private String cloudId;//对应云上数据ID
	
	public int getIsNotNull() {
		return isNotNull;
	}

	public void setIsNotNull(int isNotNull) {
		this.isNotNull = isNotNull;
	}

	public APIItem getItem() {
		return item;
	}

	public void setItem(APIItem item) {
		this.item = item;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public DataTypeEnum getType() {
		return type;
	}

	public void setType(DataTypeEnum type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
