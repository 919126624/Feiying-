package com.wuyizhiye.basedata.operationlog.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName OperationLog
 * @Description 操作日志
 * @author li.biao
 * @date 2015-4-2
 */
public class OperationLog extends CoreEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4702371857865627821L;
	
	private Date createTime; 		//操作时间
	
	private String personId ;		//操作人ID
	
	private String personNumber ;   //操作人工号
	
	private String personName ;		//操作人名称
	
	private String description;     //操作描述
	
	private String clientIp	;		//客户端IP
	
	private String url	;			//URL或 执行方法的类路径如：hr/person/list 或 com.wuyizhiye.hr.controller.HrPersonListController.list
	
	private String executeStatus;   //执行状态：成功 失败
	
	private String errorMsg;   	    //异常信息
	
	private String suffix;  //表名 自动分表用来传参
	


	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonNumber() {
		return personNumber;
	}

	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(String executeStatus) {
		this.executeStatus = executeStatus;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
}
