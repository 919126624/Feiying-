package com.wuyizhiye.basedata.param.model;

import java.util.Date;
import java.util.List;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.param.enums.ParamLevelEnum;

/**
 * @ClassName ParamHeader
 * @Description 系统参数设置实体
 * @author li.biao
 * @date 2015-4-3
 */
public class ParamHeader extends DataEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String number; //参数编号 FNUMBER	
	private String name; //参数名称  FNAME	
	private String description;//参数描述  FDESCRIPTION	
	private ParamLevelEnum level;  //参数级别 FLEVEL	
	private String module; //类型 FKMODULEID	
	private List<ParamLines> paramLines;  //参数值
	private String status ; //启用状态1启用0未启用
	
    private String syncType;//数据同步类型 CLOUD为云端数据
	
	private Date syncDate;//数据同步日期
	
	private String cloudId;//对应云上数据ID
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<ParamLines> getParamLines() {
		return paramLines;
	}
	public void setParamLines(List<ParamLines> paramLines) {
		this.paramLines = paramLines;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ParamLevelEnum getLevel() {
		return level;
	}
	public void setLevel(ParamLevelEnum level) {
		this.level = level;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
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
