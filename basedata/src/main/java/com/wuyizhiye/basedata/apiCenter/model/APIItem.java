package com.wuyizhiye.basedata.apiCenter.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName APIItem
 * @Description 接口管理
 * @author li.biao
 * @date 2015-4-2
 */
public class APIItem extends DataEntity{
	
	private static final long serialVersionUID = 1L;

	private String moduleType;//所属模块
	private String apiUrl;//接口路径
	private String paramDeclare;//参数说明
	private String returnsDeclare;//返回值说明
	
	
	private String entityJson;//参数json字符串
	
    private String syncType;//数据同步类型 CLOUD为云端数据
	
	private Date syncDate;//数据同步日期
	
	private String cloudId;//对应云上数据ID
		
	public String getEntityJson() {
		return entityJson;
	}
	public void setEntityJson(String entityJson) {
		this.entityJson = entityJson;
	}
	 
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public String getApiUrl() {
		return apiUrl;
	}
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	public String getParamDeclare() {
		return paramDeclare;
	}
	public void setParamDeclare(String paramDeclare) {
		this.paramDeclare = paramDeclare;
	}
	public String getReturnsDeclare() {
		return returnsDeclare;
	}
	public void setReturnsDeclare(String returnsDeclare) {
		this.returnsDeclare = returnsDeclare;
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
