package com.wuyizhiye.hr.salary.model;

import com.wuyizhiye.base.CoreEntity;
/**
 * 薪酬方案实施对象
 * @author hlz
 * @since 2014-02-13
 *
 */
public class SalarySchemeObject extends CoreEntity {
	private static final long serialVersionUID = -5002130462652639382L;
	/**
	 * 薪酬方案
	 */
	private SalaryScheme salaryScheme;
	/**
	 * 实施对象ID
	 */
	private String objId;
	
	public SalaryScheme getSalaryScheme() {
		return salaryScheme;
	}
	public void setSalaryScheme(SalaryScheme salaryScheme) {
		this.salaryScheme = salaryScheme;
	}
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	
	
}
