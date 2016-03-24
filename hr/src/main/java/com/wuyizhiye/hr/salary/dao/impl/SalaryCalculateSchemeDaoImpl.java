/**
 * com.wuyizhiye.hr.salary.dao.impl.SalaryCalculateSchemeDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.SalaryCalculateSchemeDao;

/**
 * @author hlz
 * 
 * @since 2014-02-19
 */
@Component(value="salaryCalculateSchemeDao")
public class SalaryCalculateSchemeDaoImpl extends BaseDaoImpl implements SalaryCalculateSchemeDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.SalaryCalculateSchemeDao";
	}
}
