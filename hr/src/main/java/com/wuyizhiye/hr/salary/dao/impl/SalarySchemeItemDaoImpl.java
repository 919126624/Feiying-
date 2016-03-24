/**
 * com.wuyizhiye.hr.salary.dao.impl.SalarySchemeItemDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.SalarySchemeItemDao;

/**
 * @author hlz
 * 
 * @since 2014-02-13
 */
@Component(value="salarySchemeItemDao")
public class SalarySchemeItemDaoImpl extends BaseDaoImpl implements SalarySchemeItemDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.SalarySchemeItemDao";
	}
}
