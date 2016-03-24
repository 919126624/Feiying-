package com.wuyizhiye.hr.salary.model;
import java.util.Date;
import java.util.List;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * 核算人员
 * @author hlz
 *
 *  @since 2014-02-19
 */
public class SalaryCalculatePerson extends CoreEntity {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 人员
	 */
	private Person person;
	
	/**
	 * 核算时所在组织
	 */
	private Org org;
	
	/**
	 * 核算时所在职位
	 */
	private Position position;
	
	/**
	 * 核算时岗位
	 */
	private Job job;
	
	/**
	 * 核算时职级
	 */
	private JobLevel jobLevel;
	
	/**
	 * 核算方案
	 */
	private SalaryCalculateScheme salaryCalculateScheme;
	
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 计薪日期
	 */
	private Date calculateDate;
	
	
	private List<SalaryCalculateItem> items;
	
	
	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public JobLevel getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(JobLevel jobLevel) {
		this.jobLevel = jobLevel;
	}

	public SalaryCalculateScheme getSalaryCalculateScheme() {
		return salaryCalculateScheme;
	}

	public void setSalaryCalculateScheme(SalaryCalculateScheme salaryCalculateScheme) {
		this.salaryCalculateScheme = salaryCalculateScheme;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<SalaryCalculateItem> getItems() {
		return items;
	}

	public void setItems(List<SalaryCalculateItem> items) {
		this.items = items;
	}

	public Date getCalculateDate() {
		return calculateDate;
	}

	public void setCalculateDate(Date calculateDate) {
		this.calculateDate = calculateDate;
	}

	
}
