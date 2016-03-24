/**
 * com.wuyizhiye.hr.salary.dao.impl.SalaryCalculatePersonDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.SalaryCalculatePersonDao;

/**
 * @author hlz
 * 
 * @since 2014-02-19
 */
@Component(value="salaryCalculatePersonDao")
public class SalaryCalculatePersonDaoImpl extends BaseDaoImpl implements SalaryCalculatePersonDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.SalaryCalculatePersonDao";
	}
}
