package com.wuyizhiye.framework.listener;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;

/**
 * @ClassName OnlineUser
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public class OnlineUser extends CoreEntity{
	private String number; //工号
	private String userName; //登录账号
	private String orgName; //登录人组织
	private String positionName; //登录人职位
	private String sessionId; //sessionId
	private String loginIp;
	private String loginRemove;	
	private String positionNum;//人员职务编号
	private String shortNum;//短号
	private String photo;//头像
	private String isOnline;// 
	private LoginTypeEnum loginType;//登陆类型
	private Org controlUnit;//CU
	
 
	public Org getControlUnit() {
		return controlUnit;
	}

	public void setControlUnit(Org controlUnit) {
		this.controlUnit = controlUnit;
	}

	public LoginTypeEnum getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginTypeEnum loginType) {
		this.loginType = loginType;
	}

	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPositionName() {
		return positionName;
	}
	
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	private String  loginDate; //登录时间
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLoginRemove() {
		return loginRemove;
	}
	public void setLoginRemove(String loginRemove) {
		this.loginRemove = loginRemove;
	}

	public String getPositionNum() {
		return positionNum;
	}

	public void setPositionNum(String positionNum) {
		this.positionNum = positionNum;
	}

	public String getShortNum() {
		return shortNum;
	}

	public void setShortNum(String shortNum) {
		this.shortNum = shortNum;
	}
}
