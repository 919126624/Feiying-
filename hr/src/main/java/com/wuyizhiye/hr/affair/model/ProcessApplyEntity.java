package com.wuyizhiye.hr.affair.model;

import java.util.Date;

import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;

/**
 * 消息中心流程数据中介类
 * @author hlz
 *
 */
public class ProcessApplyEntity {

	private String id;
	
	private String title;
	
	private PositionChangeTypeEnum changeType;
	
	private String applyName;
	
	private Date  createTime;
	
	private BillStatusEnum status;
	
	private boolean isread;
	
	private String currProcessName ;

	public boolean isIsread() {
		return isread;
	}

	public void setIsread(boolean isread) {
		this.isread = isread;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public PositionChangeTypeEnum getChangeType() {
		return changeType;
	}

	public void setChangeType(PositionChangeTypeEnum changeType) {
		this.changeType = changeType;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public BillStatusEnum getStatus() {
		return status;
	}

	public void setStatus(BillStatusEnum status) {
		this.status = status;
	}

	public String getCurrProcessName() {
		return currProcessName;
	}

	public void setCurrProcessName(String currProcessName) {
		this.currProcessName = currProcessName;
	}
}
