package com.wuyizhiye.basedata.bank.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName Bank
 * @Description 银行机构
 * @author li.biao
 * @date 2015-4-2
 */
public class Bank extends DataEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Branch branch;

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}
}
