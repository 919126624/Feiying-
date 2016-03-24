package com.wuyizhiye.basedata.apiCenter.model;


import java.util.Date;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName APIVisitLog
 * @Description 接口访问日志
 * @author li.biao
 * @date 2015-4-2
 */
public class APIVisitLog extends CoreEntity{
	
	private static final long serialVersionUID = 1L;

	//接口编码
	private String number ;
	
	//参数描述
	private String params ;
	
	//访问时间
	private Date visitTime ;
	
	//访问结果是否成功
	private int success ;
	
	//访问失败信息
	private String errorMsg ;
	
	//消耗时长（毫秒）
	private long costTime ;
	
	//端口号
	private int port ;
	
	//访问ip
	private String ip ;
	
	//动态表字段
	private String suffix ;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Date getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
}
