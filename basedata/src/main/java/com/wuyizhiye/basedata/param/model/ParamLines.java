package com.wuyizhiye.basedata.param.model;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.param.enums.ParamTypeEnum;

/**
 * @ClassName ParamLines
 * @Description  参数明细
 * @author li.biao
 * @date 2015-4-3
 */
public class ParamLines extends DataEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ParamHeader paramHeader; //参数实体	
	private String status ; //是否有效 FSTATUS	
	private String paramValue; //参数值 FPARAMVALUE	
	private Org org; //所属组织
	
	private String orgId;  //用于存储数据
	private ParamTypeEnum dataType;//参数值类型:布尔，字符串，数字，金额

	
	
	public ParamTypeEnum getDataType() {
		return dataType;
	}
	public String getDataTypeLabel() {
		if(dataType!=null){
			return dataType.getLabel();
		}else{
			return "";
		}	
	}
	public void setDataType(ParamTypeEnum dataType) {
		this.dataType = dataType;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public ParamHeader getParamHeader() {
		return paramHeader;
	}
	public void setParamHeader(ParamHeader paramHeader) {
		this.paramHeader = paramHeader;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public Org getOrg() {
		return org;
	}
	public void setOrg(Org org) {
		this.org = org;
	}
	
	
}
