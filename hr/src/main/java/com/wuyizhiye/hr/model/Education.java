package com.wuyizhiye.hr.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.hr.enums.EducationTypeEnum;

/**
 * 教育经历
 * @author taking.wang
 * @since 2013-01-15 17:11
 *
 */
public class Education extends CoreEntity{

	/**
	 * 人员
	 */
	private Person person;
	/**
	 * 开始时间
	 */
	private Date startDate;
	/**
	 * 截止时间
	 */
	private Date endDate;
	/**
	 * 教育机构
	 */
	private String educationUnit;
	/**
	 * 学历
	 */
	private EducationTypeEnum type;
	/**
	 * 证书编号
	 */
	private String certificateNumber;
	
	/**
	 * 是否是最高学历
	 */
	private boolean highest;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEducationUnit() {
		return educationUnit;
	}

	public void setEducationUnit(String educationUnit) {
		this.educationUnit = educationUnit;
	}

	public EducationTypeEnum getType() {
		return type;
	}

	public void setType(EducationTypeEnum type) {
		this.type = type;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public boolean isHighest() {
		return highest;
	}

	public void setHighest(boolean highest) {
		this.highest = highest;
	}
}
