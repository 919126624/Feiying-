package com.wuyizhiye.hr.salary.model;

import java.util.ArrayList;
import java.util.List;

import com.wuyizhiye.base.CoreEntity;

/**
 * 核算方案
 * @author hlz
 *
 *  @since 2014-02-19
 */
public class SalaryCalculateScheme extends CoreEntity {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 编码
	 */
	private String number;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 薪酬期间
	 */
	private Duration duration;		
	/**
	 * 薪酬方案
	 */
	private SalaryScheme salaryScheme;

	/**
	 * 状态
	 */
	private String state;
	
	
	private List<SalaryCalculatePerson> persons = new ArrayList<SalaryCalculatePerson>();
	
	
	public List<SalaryCalculatePerson> getPersons() {
		return persons;
	}
	public void setPersons(List<SalaryCalculatePerson> persons) {
		this.persons = persons;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Duration getDuration() {
		return duration;
	}
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	public SalaryScheme getSalaryScheme() {
		return salaryScheme;
	}
	public void setSalaryScheme(SalaryScheme salaryScheme) {
		this.salaryScheme = salaryScheme;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
