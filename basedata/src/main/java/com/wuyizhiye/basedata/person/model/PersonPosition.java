package com.wuyizhiye.basedata.person.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.model.Position;

/**
 * @ClassName PersonPosition
 * @Description 人员任职信息
 * @author li.biao
 * @date 2015-4-3
 */
public class PersonPosition extends CoreEntity {
	private static final long serialVersionUID = 3940703637836770034L;
	
	/**
	 * 职位
	 */
	private Position position;
	
	/**
	 * 职级
	 */
	private JobLevel jobLevel;
	
	/**
	 * 人员
	 */
	private Person person;
	
	/**
	 * 是否主要职位：  1表示主要职位，0表示兼职
	 */
	private boolean primary;
	
	/**
	 * 生效日期
	 */
	private Date effectDate;
	
	
	/**
	 * 分管部门ID 多一个以;分隔
	 */
	private String chargeOrgIds;
	
	/**
	 * 分管部门编码   多一个以;分隔
	 */
	private String chargeOrgNumbers;
	
	/**
	 * 分管部门名称   多一个以;分隔
	 */
	private String chargeOrgNames;
	
	
	
 
	public String getChargeOrgIds() {
		return chargeOrgIds;
	}

	public void setChargeOrgIds(String chargeOrgIds) {
		this.chargeOrgIds = chargeOrgIds;
	}

	public String getChargeOrgNumbers() {
		return chargeOrgNumbers;
	}

	public void setChargeOrgNumbers(String chargeOrgNumbers) {
		this.chargeOrgNumbers = chargeOrgNumbers;
	}

	public String getChargeOrgNames() {
		return chargeOrgNames;
	}

	public void setChargeOrgNames(String chargeOrgNames) {
		this.chargeOrgNames = chargeOrgNames;
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return the primary
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * @param primary the primary to set
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	/**
	 * @return the effectDate
	 */
	public Date getEffectDate() {
		return effectDate;
	}

	/**
	 * @param effectDate the effectDate to set
	 */
	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	public void setJobLevel(JobLevel jobLevel) {
		this.jobLevel = jobLevel;
	}

	public JobLevel getJobLevel() {
		return jobLevel;
	}
}
