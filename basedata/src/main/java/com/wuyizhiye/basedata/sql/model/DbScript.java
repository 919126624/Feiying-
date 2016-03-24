package com.wuyizhiye.basedata.sql.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.util.DateUtil;

/**
 * @ClassName DbScript
 * @Description 脚本维护实体
 * @author li.biao
 * @date 2015-4-3
 */
public class DbScript extends CoreEntity{
	
	public static final String NAMESPACE = "com.wuyizhiye.projectm.dbscript.dao.DbScriptDao";
	
	private String name;
	
	private Date createTime;
	/**
	 * 所属模块
	 */
	private String module;
	/**
	 * oracle脚本
	 */
	private String oracleScript;
	/**
	 * mysql脚本
	 */
	private String mysqlScript;
	
	private String formatCreateTime;
	
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

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * 得到格式化的创建时间
	 * @return
	 */
	public String getFormatCreateTime(){
		return DateUtil.convertDateToStr(this.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
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

	public void setFormatCreateTime(String formatCreateTime) {
		this.formatCreateTime = formatCreateTime;
	}
}
