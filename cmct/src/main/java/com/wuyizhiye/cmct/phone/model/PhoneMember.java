package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.phone.enums.PhoneControlTypeEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneEnableEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneMemberEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneMemberUseEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneStateEnum;

/**
 * @ClassName PhoneMember
 * @Description 呼叫成员
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneMember  extends DataEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5800150758463358734L;
	
	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneMemberDao";
	
	//设置类型
	private PhoneMemberEnum setType;
	
	private String mac;//所在MAC地址
	
	private PhoneStateEnum state;//状态
	
	private Person currentUser;//当前使用人
	
	private PhoneMemberUseEnum useType;//使用模式
	
	private String alias;//别名
	
	private Person onlyUser;//专用人
	
	private Date cancelDate;//停用/启用日期 日期
	
	//呼叫控制类型
	private PhoneControlTypeEnum controlType ;
	
	//呼叫人ID
	private String objectId;
	
	//呼叫人编码
	private String objectNumber ;
	
	//呼叫人名称
	private String objectName ;
	
	//呼叫接口返回唯一标识
	private String userId ;
	
	//注册手机号码
	private String loginNumber ;
	
	//接口登录密码
	private String password ;
	
	//呼叫人接听话机，多个用 ，号隔开
	private String answerPhone ;
	
	//默认接听话机
	private String defaultAnswerPhone ;
	
	//显示去电号码，多个用 ，号隔开
	private String showPhone ;
	
	//默认去电号码
	private String defaultShowPhone ;
	
	//是否登录
	private CommonFlagEnum isLogin ;
	
	//是否可用
	private PhoneEnableEnum enable ;
	
	//是否分配话伴号
	private CommonFlagEnum isAllot ;
	
	//登录orgId 
	private String orgInterfaceId ;

	
	//上线时间
	private Date onlineTime;
	
	//呼叫权限
	private PhoneRight phoneRight;
	
		
	/*------以下字段不对应数据库中字段------*/
	
	//是否默认权限,冗余字段 by lxl,14.1.11
	private String isDefaultPrivilege;
	
	//从dingjianyun分配过来号码的类型,1:华为,2:33e9,3,泽讯,页面的所有操作依据此字段调用不同的接口  by lxl 14.3.31
	private String phoneType;
	
	//电信时分配过来的一个http环境链接 by lxl 14.4.18
	private String httpUrl;
	//分配的SPID
	private String spid;
	//分配的password
	private String passWd;
	
	//orgKey
	private String orgKey;
	
	//用户新修改的手机号码//电信号码是为计费号码
	private String newPhone ;
	
	//是否回收号码
	private CommonFlagEnum isCallBank ;
	
	//核算渠道名称
	private String configName ;
	
	//月份
	private String period ;
	//话费
	private String callCost ;
	//套餐
	private String comboType ;
	
	//查询账单的时间
	private Date startTime;
	private Date endTime;
	
	/**
	 * 分配时保存的id和号码
	 * @return
	 */
	private String pmJson;
	
	/**
	 * 是否匹配过.已分配列表获取状态时
	 * @return
	 */
	private boolean match=false;
	
	/**
	 * 所绑定的副号
	 * @return
	 */
	private String billNumber;
	
	/**
	 * 营销实体,不做数据库字段,作为传参
	 * @return
	 */
	private PhoneMarket phoneMarket;
	
	/**
	 * 不做数据库字段,做显示
	 * @return
	 */
	private String name;
	
	public String getName() {
		return this.getShowPhone();
	}

	public void setName(String name) {
		this.name = name;
	}

	public PhoneMarket getPhoneMarket() {
		return phoneMarket;
	}

	public void setPhoneMarket(PhoneMarket phoneMarket) {
		this.phoneMarket = phoneMarket;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getSpid() {
		return spid;
	}

	public void setSpid(String spid) {
		this.spid = spid;
	}

	public String getPassWd() {
		return passWd;
	}

	public void setPassWd(String passWd) {
		this.passWd = passWd;
	}

	//记录拨号的sessionid 临时字段
	private String sessionId ;
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getCallCost() {
		return callCost;
	}

	public void setCallCost(String callCost) {
		this.callCost = callCost;
	}

	public String getComboType() {
		return comboType;
	}

	public void setComboType(String comboType) {
		this.comboType = comboType;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public Person getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Person currentUser) {
		this.currentUser = currentUser;
	}

	public PhoneMemberUseEnum getUseType() {
		return useType;
	}

	public void setUseType(PhoneMemberUseEnum useType) {
		this.useType = useType;
	}

	public Person getOnlyUser() {
		return onlyUser;
	}

	public void setOnlyUser(Person onlyUser) {
		this.onlyUser = onlyUser;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public PhoneStateEnum getState() {
		return state;
	}

	public void setState(PhoneStateEnum state) {
		this.state = state;
	}

	public PhoneMemberEnum getSetType() {
		return setType;
	}

	public void setSetType(PhoneMemberEnum setType) {
		this.setType = setType;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public PhoneControlTypeEnum getControlType() {
		return controlType;
	}

	public void setControlType(PhoneControlTypeEnum controlType) {
		this.controlType = controlType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectNumber() {
		return objectNumber;
	}

	public void setObjectNumber(String objectNumber) {
		this.objectNumber = objectNumber;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginNumber() {
		return loginNumber;
	}

	public void setLoginNumber(String loginNumber) {
		this.loginNumber = loginNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAnswerPhone() {
		return answerPhone;
	}

	public void setAnswerPhone(String answerPhone) {
		this.answerPhone = answerPhone;
	}

	public String getDefaultAnswerPhone() {
		return defaultAnswerPhone;
	}

	public void setDefaultAnswerPhone(String defaultAnswerPhone) {
		this.defaultAnswerPhone = defaultAnswerPhone;
	}

	public String getShowPhone() {
		return showPhone;
	}

	public void setShowPhone(String showPhone) {
		this.showPhone = showPhone;
	}

	public String getDefaultShowPhone() {
		return defaultShowPhone;
	}

	public void setDefaultShowPhone(String defaultShowPhone) {
		this.defaultShowPhone = defaultShowPhone;
	}

	public CommonFlagEnum getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(CommonFlagEnum isLogin) {
		this.isLogin = isLogin;
	}

	public PhoneEnableEnum getEnable() {
		return enable;
	}

	public void setEnable(PhoneEnableEnum enable) {
		this.enable = enable;
	}

	public CommonFlagEnum getIsAllot() {
		return isAllot;
	}

	public void setIsAllot(CommonFlagEnum isAllot) {
		this.isAllot = isAllot;
	}

	public String getNewPhone() {
		return newPhone;
	}

	public void setNewPhone(String newPhone) {
		this.newPhone = newPhone;
	}

	public String getOrgInterfaceId() {
		return orgInterfaceId;
	}

	public void setOrgInterfaceId(String orgInterfaceId) {
		this.orgInterfaceId = orgInterfaceId;
	}

	public CommonFlagEnum getIsCallBank() {
		return isCallBank;
	}

	public void setIsCallBank(CommonFlagEnum isCallBank) {
		this.isCallBank = isCallBank;
	}

	public Date getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}

	public PhoneRight getPhoneRight() {
		return phoneRight;
	}

	public void setPhoneRight(PhoneRight phoneRight) {
		this.phoneRight = phoneRight;
	}

	public String getIsDefaultPrivilege() {
		return isDefaultPrivilege;
	}

	public void setIsDefaultPrivilege(String isDefaultPrivilege) {
		this.isDefaultPrivilege = isDefaultPrivilege;
	}

	public String getPmJson() {
		return pmJson;
	}

	public void setPmJson(String pmJson) {
		this.pmJson = pmJson;
	}

	public boolean isMatch() {
		return match;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getOrgKey() {
		return orgKey;
	}

	public void setOrgKey(String orgKey) {
		this.orgKey = orgKey;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
