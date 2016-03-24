package com.wuyizhiye.cmct.wechat.model;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName RecruitInfo
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class RecruitInfo extends CoreEntity{
	/**
	 * 招募人
	 */
	private Person person;
	/**
	 * 招募的人数
	 */
	private Integer recruitCount;
	/**
	 * 招募名单
	 */
	private String recruitRemark;
	
	
	/** 冗余字段 **/
	/**
	 * 招募本人描述
	 */
	private String personDesc;
	
	
	
	public String getPersonDesc() {
		String orgName = personDesc;
		if(personDesc.contains("(")){
			return personDesc;
		}
		personDesc="";
		if(StringUtils.isNotNull(person.getName())){
			personDesc +=person.getName(); 
		}
		if(StringUtils.isNotNull(orgName)){
			personDesc +="("+orgName+")";
		}
		return personDesc;
	}
	public void setPersonDesc(String personDesc) {
		this.personDesc = personDesc;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Integer getRecruitCount() {
		return recruitCount;
	}
	public void setRecruitCount(Integer recruitCount) {
		this.recruitCount = recruitCount;
	}
	public String getRecruitRemark() {
		return recruitRemark;
	}
	public void setRecruitRemark(String recruitRemark) {
		this.recruitRemark = recruitRemark;
	}
	
}
