package com.wuyizhiye.hr.salary.model;

import com.wuyizhiye.base.CoreEntity;

/**
 *  薪酬标准 项目 明细
 * @author hly
 *
 */
public class StandardItem extends CoreEntity{
	
	private SalaryStandard salarayStandard;//对应薪酬标准
	
	private SalaryItem salaryItem;//对应 薪酬项目
	
	private Double amount;//金额

	public SalaryStandard getSalarayStandard() {
		return salarayStandard;
	}

	public void setSalarayStandard(SalaryStandard salarayStandard) {
		this.salarayStandard = salarayStandard;
	}

	public SalaryItem getSalaryItem() {
		return salaryItem;
	}

	public void setSalaryItem(SalaryItem salaryItem) {
		this.salaryItem = salaryItem;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
