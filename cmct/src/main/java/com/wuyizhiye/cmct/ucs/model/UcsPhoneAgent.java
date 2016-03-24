package com.wuyizhiye.cmct.ucs.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName UcsPhoneAgent
 * @Description 坐席归属,企业用户表,
 * @author li.biao
 * @date 2015-5-26
 */
public class UcsPhoneAgent extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 企业用户名称
	 */
	private String agentName;
	
	/**
	 * 企业agentId
	 */
	private String agentId;
	
	/**
	 * 企业登录密码
	 */
	private String passwd;
	
	/**
	 * 代理商id,即为经销商
	 */
	private String dealerId;
	
	/**
	 * 创建用户
	 */
	private Person creator;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 代理类型
	 */
	private Integer dealerType;
	
	/**
	 * 联系电话
	 */
	private String telNo;
	
	/**
	 * 加密字符串
	 */
	private String key;
	
	/**
	 * 所属经销商的名称
	 * @return
	 */
	private String dealerName;

	/**
	 * calledCheck  被叫是否审核
	 */
	private Integer calledCheck;
	
	/**
	 * clientNumber 客户号码是否可见
	 */
	private Integer clientNumber;
	/**
	 * 
	 */
	//------做显示用
	private String name;
	
	/**
	 * 所属上级经销商
	 */
	private String parentAgent;
	/**
	 * 删除企业用户时用到的上级的经销商的userName;
	 * @return
	 */
	private String dealerUserName;
	
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getDealerId() {
		return dealerId;
	}

	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getDealerType() {
		return dealerType;
	}

	public void setDealerType(Integer dealerType) {
		this.dealerType = dealerType;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getName() {
		return this.getAgentName();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDealerUserName() {
		return dealerUserName;
	}

	public void setDealerUserName(String dealerUserName) {
		this.dealerUserName = dealerUserName;
	}

	public String getParentAgent() {
		return parentAgent;
	}

	public void setParentAgent(String parentAgent) {
		this.parentAgent = parentAgent;
	}

	public Integer getCalledCheck() {
		return calledCheck;
	}

	public void setCalledCheck(Integer calledCheck) {
		this.calledCheck = calledCheck;
	}

	public Integer getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(Integer clientNumber) {
		this.clientNumber = clientNumber;
	}
	
}
