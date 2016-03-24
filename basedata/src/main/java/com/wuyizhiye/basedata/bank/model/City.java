package com.wuyizhiye.basedata.bank.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName City
 * @Description 所属城市  
 * @author li.biao
 * @date 2015-4-2
 */
public class City extends DataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name; // 名称
	private String simpleName; // 简称名称
	private String fullName; // 简称名称
	private City parent;//父级城市
	private String isModel;//by lxl 14.12.18 是否参与建模 Y:参与建模,其它则不是
	
    private String syncType;//数据同步类型 CLOUD为云端数据
	
	private Date syncDate;//数据同步日期
	
	private String cloudId;//对应云上数据ID
	
	public String getIsModel() {
		return isModel;
	}
	public void setIsModel(String isModel) {
		this.isModel = isModel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	public City getParent() {
		return parent;
	}
	public void setParent(City parent) {
		this.parent = parent;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
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
