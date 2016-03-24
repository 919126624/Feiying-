package com.wuyizhiye.hr.salary.model;

import java.math.BigDecimal;

import com.wuyizhiye.base.CoreEntity;

/**
 * 核算项目
 * @author hlz
 *
 *  @since 2014-02-19
 */
public class SalaryCalculateItem extends CoreEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 核算人员
	 */
	private SalaryCalculatePerson salaryCalculatePerson;
	/**
	 * 薪酬项目
	 */
	private SalaryItem salaryItem;
	/**
	 * 工资
	 */
	private BigDecimal money;
	/**
	 * 顺序
	 */
	private Integer order;	
	
	public SalaryCalculatePerson getSalaryCalculatePerson() {
		return salaryCalculatePerson;
	}
	public void setSalaryCalculatePerson(SalaryCalculatePerson salaryCalculatePerson) {
		this.salaryCalculatePerson = salaryCalculatePerson;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public SalaryItem getSalaryItem() {
		return salaryItem;
	}
	public void setSalaryItem(SalaryItem salaryItem) {
		this.salaryItem = salaryItem;
	}
}
