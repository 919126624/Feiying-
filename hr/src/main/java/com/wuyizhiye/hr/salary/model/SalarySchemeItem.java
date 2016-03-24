package com.wuyizhiye.hr.salary.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * 薪酬方案项目
 * @author hlz
 * @since 2014-02-13
 *
 */
public class SalarySchemeItem extends CoreEntity {
	private static final long serialVersionUID = -5002130462652639382L;
	
	/**
	 * 薪酬方案
	 */
	private SalaryScheme salaryScheme;
	/**
	 * 薪酬项目
	 */
	private SalaryItem salaryItem;
	/**
	 * 公式状态
	 */
	private int formulaState;
	/**
	 * 公式
	 */
	private String formula;
	/**
	 * 公式翻译后的脚本
	 */
	private String script;
	
	private int order;

	public SalaryScheme getSalaryScheme() {
		return salaryScheme;
	}

	public void setSalaryScheme(SalaryScheme salaryScheme) {
		this.salaryScheme = salaryScheme;
	}

	public SalaryItem getSalaryItem() {
		return salaryItem;
	}

	public void setSalaryItem(SalaryItem salaryItem) {
		this.salaryItem = salaryItem;
	}

	public int getFormulaState() {
		return formulaState;
	}

	public void setFormulaState(int formulaState) {
		this.formulaState = formulaState;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	

}
