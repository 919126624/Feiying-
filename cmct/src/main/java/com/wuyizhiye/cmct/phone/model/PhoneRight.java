package com.wuyizhiye.cmct.phone.model;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;

/**
 * @ClassName PhoneRight
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneRight extends DataEntity {

	private static final long serialVersionUID = 3948431322625428966L;
	
	//是否允许呼叫本地手机
	private String localMob;
	
	//是否允许呼叫本地固话
	private String localFixed;
	
	//是否允许呼叫国内长途
	private String domestic;

	//呼叫国际长途模板ID
	private String interTempletID;
	
	//是否允许隐藏呼出
	private String hide;
	
	//黑名单
	private String black;
	
	//核算渠道ORG ID
	private String callOrgId;
	
	private String callOrgName;
	
	//核算渠道name
	private String configName;
	
	//接口权限ID
	private String callRightId;
	
	private String phonenum;
	
	//是否核算渠道组织默认
	private CommonFlagEnum orgDefault ;
	
	public CommonFlagEnum getOrgDefault() {
		return orgDefault;
	}

	public void setOrgDefault(CommonFlagEnum orgDefault) {
		this.orgDefault = orgDefault;
	}

	public String getLocalMob() {
		return localMob;
	}

	public void setLocalMob(String localMob) {
		this.localMob = localMob;
	}

	public String getLocalFixed() {
		return localFixed;
	}

	public void setLocalFixed(String localFixed) {
		this.localFixed = localFixed;
	}

	public String getDomestic() {
		return domestic;
	}

	public void setDomestic(String domestic) {
		this.domestic = domestic;
	}

	public String getInterTempletID() {
		return interTempletID;
	}

	public void setInterTempletID(String interTempletID) {
		this.interTempletID = interTempletID;
	}

	public String getHide() {
		return hide;
	}

	public void setHide(String hide) {
		this.hide = hide;
	}

	public String getBlack() {
		return black;
	}

	public void setBlack(String black) {
		this.black = black;
	}

	public String getCallOrgId() {
		return callOrgId;
	}

	public void setCallOrgId(String callOrgId) {
		this.callOrgId = callOrgId;
	}

	public String getCallOrgName() {
		return callOrgName;
	}

	public void setCallOrgName(String callOrgName) {
		this.callOrgName = callOrgName;
	}
	
	public String getCallRightId() {
		return callRightId;
	}

	public void setCallRightId(String callRightId) {
		this.callRightId = callRightId;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	
	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getAddParaStr(){
		return "localMob="
				+ this.getLocalMob()
				+ "&localFixed="+this.getLocalFixed()
				+"&domestic="+this.getDomestic()
				+"&interTempletID="+this.getInterTempletID()
				+"&hide="+this.getHide()+(StringUtils.isEmpty(this.getBlack())?"":("&black="+this.getBlack()));
	}
	
	public String getUpdParaStr(){
		return "localMob="
				+ this.getLocalMob()
				+ "&localFixed="+this.getLocalFixed()
				+"&domestic="+this.getDomestic()
				+"&interTempletID="+this.getInterTempletID()
				+"&hide="+this.getHide()+(StringUtils.isEmpty(this.getBlack())?"":("&black="+this.getBlack()))+"&callRightsID="+this.getCallRightId();
	}
	
	public String getDelStr(){
		return "callRightsID="+this.getCallRightId();
	}
	
	public String getBindStr(){
		return "callRightsID="+this.getCallRightId()+"&phone="+this.getPhonenum();
	}
	
	public String getUnBindStr(){
		return "callRightsID="+this.getCallRightId()+"&phone="+this.getPhonenum();
	}
	
	public String getOrgDefaultStr(){
		return "callRightsID="+this.getCallRightId();
	}
}
