/**
 * com.wuyizhiye.hr.salary.dao.impl.SalarySchemeObjectDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.SalarySchemeObjectDao;

/**
 * @author hlz
 * 
 * @since 2014-02-13
 */
@Component(value="salarySchemeObjectDao")
public class SalarySchemeObjectDaoImpl extends BaseDaoImpl implements SalarySchemeObjectDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.SalarySchemeObjectDao";
	}
}
