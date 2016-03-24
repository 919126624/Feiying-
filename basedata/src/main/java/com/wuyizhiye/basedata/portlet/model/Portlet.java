package com.wuyizhiye.basedata.portlet.model;

import java.util.Date;

import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.portlet.enums.FormatSizeEnum;
import com.wuyizhiye.basedata.portlet.enums.LayoutEnum;
import com.wuyizhiye.basedata.portlet.enums.PortletStatusEnum;

/**
 * @ClassName Portlet
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class Portlet extends DataEntity {
	
	private String url;
	private FormatSizeEnum formatSize;
	private String jsName;
	private PortletStatusEnum status;
	
	private BusinessTypeEnum businessType;//所属业务类型
	private String configUrl;//配置路径   
	private String configPageSize;//配置大小（宽X高） 
	private LayoutEnum layout;//首页布局模式
	
    private String syncType;//数据同步类型 CLOUD为云端数据
	
	private Date syncDate;//数据同步日期
	
	private String cloudId;//对应云上数据ID
	
	public String getConfigPageSize() {
		return configPageSize;
	}

	public void setConfigPageSize(String configPageSize) {
		this.configPageSize = configPageSize;
	}

	public LayoutEnum getLayout() {
		return layout;
	}

	public void setLayout(LayoutEnum layout) {
		this.layout = layout;
	}

	public BusinessTypeEnum getBusinessType() {
		return businessType;
	}

	public void setBusinessType(BusinessTypeEnum businessType) {
		this.businessType = businessType;
	}

	public String getConfigUrl() {
		return configUrl;
	}

	public void setConfigUrl(String configUrl) {
		this.configUrl = configUrl;
	}

	public Portlet(){
		
	}
	
	public Portlet(String id){
		this.setId(id);
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public FormatSizeEnum getFormatSize() {
		return formatSize;
	}
	public void setFormatSize(FormatSizeEnum formatSize) {
		this.formatSize = formatSize;
	}
	public String getJsName() {
		return jsName;
	}
	public void setJsName(String jsName) {
		this.jsName = jsName;
	}
	public PortletStatusEnum getStatus() {
		return status;
	}
	public void setStatus(PortletStatusEnum status) {
		this.status = status;
	}
	
	public String getFormatSizeStr(){
		return this.getFormatSize().getTitle();
	}
	
	public String getStatusStr(){
		return this.getStatus().getName();
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
