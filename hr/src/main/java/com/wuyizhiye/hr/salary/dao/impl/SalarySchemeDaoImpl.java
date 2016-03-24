/**
 * com.wuyizhiye.hr.salary.dao.impl.SalarySchemeDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.SalarySchemeDao;

/**
 * @author hlz
 * 
 * @since 2014-02-13
 */
@Component(value="salarySchemeDao")
public class SalarySchemeDaoImpl extends BaseDaoImpl implements SalarySchemeDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.SalarySchemeDao";
	}
}
