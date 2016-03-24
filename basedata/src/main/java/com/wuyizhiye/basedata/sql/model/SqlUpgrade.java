package com.wuyizhiye.basedata.sql.model;


import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.sql.enums.SqlRunStatusEnum;

/**
 * @ClassName SqlUpgrade
 * @Description sql升级
 * @author li.biao
 * @date 2015-4-3
 */
public class SqlUpgrade extends CoreEntity{
	private String name;
	private Date createTime;
	private Date runTime;
	private SqlRunStatusEnum runStatus;
	private String module; //FKMODULEID 模块类型
	private String oracleScript;
	private String mysqlScript;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getRunTime() {
		return runTime;
	}
	public void setRunTime(Date runTime) {
		this.runTime = runTime;
	}
	public SqlRunStatusEnum getRunStatus() {
		return runStatus;
	}
	public void setRunStatus(SqlRunStatusEnum runStatus) {
		this.runStatus = runStatus;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getOracleScript() {
		return oracleScript;
	}
	public void setOracleScript(String oracleScript) {
		this.oracleScript = oracleScript;
	}
	public String getMysqlScript() {
		return mysqlScript;
	}
	public void setMysqlScript(String mysqlScript) {
		this.mysqlScript = mysqlScript;
	}
	
	/**
	 * 得到格式化的创建时间
	 * @return
	 */
	public String getFormatCreateTime(){
		return DateUtil.convertDateToStr(this.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 得到格式化的运行时间
	 * @return
	 */
	public String getFormatRunTime(){
		return DateUtil.convertDateToStr(this.getRunTime(), "yyyy-MM-dd HH:mm:ss");
	}
	
	public String getRunStatusDesc(){
		return null==this.getRunStatus()?"":this.getRunStatus().getLabel();
	}
	
	public String getModuleDesc(){
		return null==this.getModule()?"":ModuleEnum.valueOf(this.getModule()).getName();
	}
}
