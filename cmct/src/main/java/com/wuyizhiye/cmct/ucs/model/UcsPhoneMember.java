package com.wuyizhiye.cmct.ucs.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.phone.enums.PhoneEnableEnum;
import com.wuyizhiye.cmct.ucs.enums.UcsPhoneFlagEnum;

/**
 * @ClassName UcsPhoneMember
 * @Description 固话号码 (对于ucs接口的坐席数据)
 * @author li.biao
 * @date 2015-5-26
 */
public class UcsPhoneMember extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 坐席编号
	 */
	private String userNo;
	
	/**
	 * 坐席姓名
	 */
	private String userName;
	
	/**
	 * 	坐席电话
	 */
	private String telNo;
	
	/**
	 * 类别
	 */
	private Integer userType;
	
	/**
	 * 登录密码
	 */
	private String passwd;
	
	/**
	 * 坐席备注
	 */
	private String remark;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 创建用户
	 */
	private Person creator;
	
	/**
	 * 使用组织
	 */
	private Org org;
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	
	/**
	 * 最后更新用户
	 */
	private Person updator;
	
	/**
	 * 坐席归属,企业用户
	 */
	private UcsPhoneAgent ucsPhoneAgent;

	/**
	 * 禁用启用
	 */
	private PhoneEnableEnum enable ;
	
	private String key;
	/**
	 * 状态
	 * @return
	 */
	private UcsPhoneFlagEnum state;
	
	private Person onlyUser;//专用人
	
	private Person currentUser;//当前使用人
	
	private Date cancelDate;//停用/启用日期 日期
	/**
	 * 去电显示号码
	 */
	private UcsPhoneShowTel showTel;
	
	/**
	 * 占用状态
	 */
	private CommonFlagEnum flag;
	//--------------不对应数据库
	/**
	 * 对方号码
	 */
	private String callNo;
	
	/**
	 * 禁用或则企业,默认为启用
	 */
	private boolean userDisable=true;
	/**
	 * 语音记录下载
	 * 录音文件全称
	 * @return
	 */
	private String svmixFullName;
	
	
	//不对应数据库
	private String ip;
	
	private String showTelNo;
	
	//flagId,标志的id,修改企业用户时保存到的,用于区分id
	private String flagId;
	
	/**
	 * ucsPhoneShowTelNo
	 * @return
	 */
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Person getCreator() {
		return creator;
	}
	public void setCreator(Person creator) {
		this.creator = creator;
	}
	public Org getOrg() {
		return org;
	}
	public void setOrg(Org org) {
		this.org = org;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Person getUpdator() {
		return updator;
	}
	public void setUpdator(Person updator) {
		this.updator = updator;
	}
	public UcsPhoneAgent getUcsPhoneAgent() {
		return ucsPhoneAgent;
	}
	public void setUcsPhoneAgent(UcsPhoneAgent ucsPhoneAgent) {
		this.ucsPhoneAgent = ucsPhoneAgent;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public UcsPhoneFlagEnum getState() {
		return state;
	}
	public void setState(UcsPhoneFlagEnum state) {
		this.state = state;
	}
	public String getCallNo() {
		return callNo;
	}
	public void setCallNo(String callNo) {
		this.callNo = callNo;
	}
	public String getSvmixFullName() {
		return svmixFullName;
	}
	public void setSvmixFullName(String svmixFullName) {
		this.svmixFullName = svmixFullName;
	}
	public Person getOnlyUser() {
		return onlyUser;
	}
	public void setOnlyUser(Person onlyUser) {
		this.onlyUser = onlyUser;
	}
	public Person getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(Person currentUser) {
		this.currentUser = currentUser;
	}
	public CommonFlagEnum getFlag() {
		return flag;
	}
	public void setFlag(CommonFlagEnum flag) {
		this.flag = flag;
	}
	
	public UcsPhoneShowTel getShowTel() {
		return showTel;
	}
	public void setShowTel(UcsPhoneShowTel showTel) {
		this.showTel = showTel;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public boolean isUserDisable() {
		return userDisable;
	}
	public void setUserDisable(boolean userDisable) {
		this.userDisable = userDisable;
	}
	public PhoneEnableEnum getEnable() {
		return enable;
	}
	public void setEnable(PhoneEnableEnum enable) {
		this.enable = enable;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getShowTelNo() {
		return showTelNo;
	}
	public void setShowTelNo(String showTelNo) {
		this.showTelNo = showTelNo;
	}
	public String getFlagId() {
		return flagId;
	}
	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}
	
}
