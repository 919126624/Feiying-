package com.wuyizhiye.basedata.code.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.code.enums.FetchTypeEnum;

/**
 * @ClassName PrintConfig
 * @Description 打印配置  
 * @author li.biao
 * @date 2015-4-2
 */
public class PrintConfig extends DataEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 所属模块
	 */
	private String module; //类型 FKMODULEID	
	
	/**
	 * 关联单据类型
	 */
	private BillType billType;
	
	/**
	 * 取数方式
	 */
	private FetchTypeEnum fetchType;
	  
	/**
	 * 取数配置
	 */
	private String fetchConfig;
	
	/**
	 * 打印模板（富文本）
	 */
	private String printModel;

    private String syncType;//数据同步类型 CLOUD为云端数据
	
	private Date syncDate;//数据同步日期
	
	private String cloudId;//对应云上数据ID
	
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public BillType getBillType() {
		return billType;
	}

	public void setBillType(BillType billType) {
		this.billType = billType;
	}

	public FetchTypeEnum getFetchType() {
		return fetchType;
	}

	public void setFetchType(FetchTypeEnum fetchType) {
		this.fetchType = fetchType;
	}

	public String getFetchConfig() {
		return fetchConfig;
	}

	public void setFetchConfig(String fetchConfig) {
		this.fetchConfig = fetchConfig;
	}

	public String getPrintModel() {
		return printModel;
	}

	public void setPrintModel(String printModel) {
		this.printModel = printModel;
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
