package com.wuyizhiye.basedata.application.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.application.enums.ApplicationStatusEnum;

/**
 * @ClassName MacApplication
 * @Description Mac申请
 * @author li.biao
 * @date 2015-4-2
 */
public class MacApplication extends CoreEntity {
	
	private static final long serialVersionUID = 3987541952199036346L;

	/**
	 * 申请时间
	 */
	private Date aplTime;
	
	/**
	 * 申请唯一识别码
	 */
	private String mac;
	
	/**
	 * 产生随机码
	 */
	private String randomNumber;
	
	/**
	 * 审批状态
	 */
	private ApplicationStatusEnum status;


	public Date getAplTime() {
		return aplTime;
	}

	public void setAplTime(Date aplTime) {
		this.aplTime = aplTime;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getRandomNumber() {
		return randomNumber;
	}

	public void setRandomNumber(String randomNumber) {
		this.randomNumber = randomNumber;
	}

	public ApplicationStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatusEnum status) {
		this.status = status;
	}
	
}
