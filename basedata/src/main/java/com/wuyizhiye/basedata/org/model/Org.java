package com.wuyizhiye.basedata.org.model;

import java.util.Date;

import com.wuyizhiye.basedata.TreeEntity;
import com.wuyizhiye.basedata.org.enums.OrgStatusEnum;
import com.wuyizhiye.basedata.org.enums.OrgTypeEnum;

/**
 * @ClassName Org
 * @Description 组织
 * @author li.biao
 * @date 2015-4-2
 */
public class Org extends TreeEntity<Org> {
	private static final long serialVersionUID = -5945351064703581026L;

	/**
	 * 全称
	 */
	private String displayName; 
	
	/**
	 * 组织状态
	 */
	private OrgStatusEnum status;
	
	/**
	 * 控制单元
	 */
	private Boolean cu;
	
	/**
	 * 生效日期
	 */
	private Date effectiveDate; 
	
	/**
	 * 失效日期
	 */
	private Date disabledDate; 
	
	/**
	 * 组织类型
	 */
	private OrgTypeEnum orgType;
	
	/**
	 * 业务类型
	 */
	private String businessTypes;
	/**
	 * 业务类型名称
	 */
	private String businessTypesName;
	/**
	 * 包含岗位
	 */
	private String jobs;
	/**
	 * 组织级别描述ID
	 */
	private String orgLevelDesc;
	
	/**
	 * 组织级别名
	 */
	private String orgLevelName;
	
	/**
	 * 全拼
	 */
	private String fullPinyin;
	
	/**
	 * by lxl 14.11.19 用于复制新增的时候的orgId,传值
	 */
	private String copyOrgId;
	/**
	 * by wcx 14.12.23 新增字段管辖城市ids,联系电话,组织地址
	 */
	private String cityIds;
	
	private String telnum;//电话
	
	private String address;//地址
	
	private String personName;
	/**
	 * by wcx 15.02.12 新增地图标点字段
	 */
	private String mapx;// X坐标
	private String mapy;// Y坐标
	private Integer zoom;// 缩放级别
	private Integer mapoint;// 是否地图标点
	/**----新增字段   经纬度hash匹配码-----*/
	private String geoHashStr;
	
	
	
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getCopyOrgId() {
		return copyOrgId;
	}

	public void setCopyOrgId(String copyOrgId) {
		this.copyOrgId = copyOrgId;
	}

	public String getFullPinyin() {
		return fullPinyin;
	}

	public void setFullPinyin(String fullPinyin) {
		this.fullPinyin = fullPinyin;
	}

	public String getOrgLevelDesc() {
		return orgLevelDesc;
	}

	public void setOrgLevelDesc(String orgLevelDesc) {
		this.orgLevelDesc = orgLevelDesc;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setDisabledDate(Date disabledDate) {
		this.disabledDate = disabledDate;
	}

	public Date getDisabledDate() {
		return disabledDate;
	}

	public Org getParent() {
		return this.parent;
	}
	
	public void setParent(Org parent) {
		this.parent = parent;
	}

	public void setStatus(OrgStatusEnum status) {
		this.status = status;
	}

	public OrgStatusEnum getStatus() {
		return status;
	}

	public void setCu(Boolean cu) {
		this.cu = cu;
	}

	public Boolean getCu() {
		return cu;
	}

	public void setJobs(String jobs) {
		this.jobs = jobs;
	}

	public String getJobs() {
		return jobs;
	}

	public OrgTypeEnum getOrgType() {
		return orgType;
	}

	public void setOrgType(OrgTypeEnum orgType) {
		this.orgType = orgType;
	}

	public String getBusinessTypes() {
		return businessTypes;
	}

	public void setBusinessTypes(String businessTypes) {
		this.businessTypes = businessTypes;
	}

	public String getBusinessTypesName() {
		return businessTypesName;
	}

	public void setBusinessTypesName(String businessTypesName) {
		this.businessTypesName = businessTypesName;
	}

	public String getOrgLevelName() {
		return orgLevelName;
	}

	public void setOrgLevelName(String orgLevelName) {
		this.orgLevelName = orgLevelName;
	}

	public String getCityIds() {
		return cityIds;
	}

	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}

	public String getTelnum() {
		return telnum;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMapx() {
		return mapx;
	}

	public void setMapx(String mapx) {
		this.mapx = mapx;
	}

	public String getMapy() {
		return mapy;
	}

	public void setMapy(String mapy) {
		this.mapy = mapy;
	}

	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}

	public Integer getMapoint() {
		return mapoint;
	}

	public void setMapoint(Integer mapoint) {
		this.mapoint = mapoint;
	}

	public String getGeoHashStr() {
		return geoHashStr;
	}

	public void setGeoHashStr(String geoHashStr) {
		this.geoHashStr = geoHashStr;
	}
	
}
