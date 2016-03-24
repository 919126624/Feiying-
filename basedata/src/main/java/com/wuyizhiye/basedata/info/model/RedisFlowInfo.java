package com.wuyizhiye.basedata.info.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName RedisFlowInfo
 * @Description 流程相关信息
 * @author li.biao
 * @date 2015-4-2
 */
public class RedisFlowInfo extends CoreEntity  {

	private String personId;	//人员Id
	private String positionId;	//职位Id	//职位Id
	private int flowCount;		//流程数量
	private Boolean  flowNeedFloatDlg =false;	//流程是否允许弹窗	默认是不弹窗
	private String flowLastContent;		//流程最后内容
	
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public int getFlowCount() {
		return flowCount;
	}
	public void setFlowCount(int flowCount) {
		this.flowCount = flowCount;
	}
	public Boolean getFlowNeedFloatDlg() {
		return flowNeedFloatDlg;
	}
	public void setFlowNeedFloatDlg(Boolean flowNeedFloatDlg) {
		this.flowNeedFloatDlg = flowNeedFloatDlg;
	}
	public String getFlowLastContent() {
		return flowLastContent;
	}
	public void setFlowLastContent(String flowLastContent) {
		this.flowLastContent = flowLastContent;
	}
	
	
}
