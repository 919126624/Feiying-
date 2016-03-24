package com.wuyizhiye.hr.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.hr.enums.AgentTypeEnum;
import com.wuyizhiye.hr.enums.CertificateTypeEnum;
import com.wuyizhiye.hr.enums.MemberLevelEnum;

/**
 * 经纪人证
 * @author taking.wang
 * @since 2013-01-15
 *
 */
public class AgentCertificate extends CoreEntity {

	/**
	 * 人员
	 */
	private Person person;
	/**
	 * 证件类型
	 */
	private CertificateTypeEnum type;
	/**
	 * 证件编号
	 */
	private String number;
	/**
	 * 考取时间
	 */
	private Date passDate;
	/**
	 * 经纪人类型
	 */
	private AgentTypeEnum agentType;
	/**
	 * 会员级别
	 */
	private MemberLevelEnum memberLevel;
	/**
	 * 有效期
	 */
	private String effectPeriod;
	/**
	 * 年检日期
	 */
	private Date checkDate;
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Date getPassDate() {
		return passDate;
	}
	public void setPassDate(Date passDate) {
		this.passDate = passDate;
	}
	public String getEffectPeriod() {
		return effectPeriod;
	}
	public void setEffectPeriod(String effectPeriod) {
		this.effectPeriod = effectPeriod;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public AgentTypeEnum getAgentType() {
		return agentType;
	}
	public void setAgentType(AgentTypeEnum agentType) {
		this.agentType = agentType;
	}
	public MemberLevelEnum getMemberLevel() {
		return memberLevel;
	}
	public void setMemberLevel(MemberLevelEnum memberLevel) {
		this.memberLevel = memberLevel;
	}
	public CertificateTypeEnum getType() {
		return type;
	}
	public void setType(CertificateTypeEnum type) {
		this.type = type;
	}
}
