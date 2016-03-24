package com.wuyizhiye.cmct.ucs.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.phone.enums.StateEnum;

/**
 * @ClassName UcsPhoneShowTel
 * @Description 去电显示号码实体
 * @author li.biao
 * @date 2015-5-26
 */
public class UcsPhoneShowTel extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 显示号码
	 */
	private String showTelNo;
	
	/**
	 * 当前的状态是否有被选择,YES:已选择,NO:还未选择
	 */
	private StateEnum state;
	
	/**
	 * 审核状态   YES:已审核,NO:未审核
	 */
	private StateEnum audit;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 创建用户
	 */
	private Person creator;
	
	/**
	 * 所创建的企业id
	 */
	private UcsPhoneAgent agent;

	
	//以下属性不对应数据库
	
	private String name;
	
	//保存时用到
	private String contactJson;
	
	//企业 用户名:冗余
	
	private String agentId;
	
	/**
	 * 一长串的showTelNo,用,隔开
	 * @return
	 */
	private String showTelNoMore;
	
	public String getShowTelNo() {
		return showTelNo;
	}

	public void setShowTelNo(String showTelNo) {
		this.showTelNo = showTelNo;
	}

	public StateEnum getState() {
		return state;
	}

	public void setState(StateEnum state) {
		this.state = state;
	}

	public StateEnum getAudit() {
		return audit;
	}

	public void setAudit(StateEnum audit) {
		this.audit = audit;
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

	public UcsPhoneAgent getAgent() {
		return agent;
	}

	public void setAgent(UcsPhoneAgent agent) {
		this.agent = agent;
	}

	public String getName() {
		return this.showTelNo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactJson() {
		return contactJson;
	}

	public void setContactJson(String contactJson) {
		this.contactJson = contactJson;
	}
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getShowTelNoMore() {
		return showTelNoMore;
	}

	public void setShowTelNoMore(String showTelNoMore) {
		this.showTelNoMore = showTelNoMore;
	}
	
}
