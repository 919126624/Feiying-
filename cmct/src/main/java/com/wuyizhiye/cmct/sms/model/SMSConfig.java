package com.wuyizhiye.cmct.sms.model;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.cmct.sms.enums.SMSChannelTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSControlTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSTypeEnum;

/**
 * @ClassName SMSConfig
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class SMSConfig extends CoreEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1217082273195259247L;

	private SMSControlTypeEnum  controlType; //控制类型  个人、部门、公司    
	
	private SMSTypeEnum type ;//短信类型  业务、广告  
	
	private String groupId ; //企业ID
	
	private String accountName ;//账号
	
	private String url ;// 发送短信url
	
	private String replyUrl;//查看回复短信url
	
	private String statusUrl;//获取发送状态url
	
	private String password ; //密码
	
	private String beanId ; //短信服务提供商类ID
	
	private String methodName ; //接口方法
	
	private String description ; //描述
	
	private Boolean enableFlag ; //启用 、禁用标识
	
	
    /**
     * 14.9.5 通道类型
     */
	private SMSChannelTypeEnum  channelType;
	
	/**
	 * 批送最低数量
	 */
	private Integer batchMinNum;
	
	public SMSChannelTypeEnum getChannelType() {
		return channelType;
	}

	public void setChannelType(SMSChannelTypeEnum channelType) {
		this.channelType = channelType;
	}

	public Integer getBatchMinNum() {
		return batchMinNum;
	}

	public void setBatchMinNum(Integer batchMinNum) {
		this.batchMinNum = batchMinNum;
	}

	public String getStatusUrl() {
		return statusUrl;
	}

	public void setStatusUrl(String statusUrl) {
		this.statusUrl = statusUrl;
	}

	public String getReplyUrl() {
		return replyUrl;
	}

	public void setReplyUrl(String replyUrl) {
		this.replyUrl = replyUrl;
	}

	public SMSControlTypeEnum getControlType() {
		return controlType;
	}

	public void setControlType(SMSControlTypeEnum controlType) {
		this.controlType = controlType;
	}

	public SMSTypeEnum getType() {
		return type;
	}

	public void setType(SMSTypeEnum type) {
		this.type = type;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBeanId() {
		return beanId;
	}

	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(Boolean enableFlag) {
		this.enableFlag = enableFlag;
	}
	
}
