/**
 * com.wuyizhiye.hr.salary.dao.impl.SalaryCalculateItemDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.SalaryCalculateItemDao;

/**
 * @author hlz
 * 
 * @since 2014-02-19
 */
@Component(value="salaryCalculateItemDao")
public class SalaryCalculateItemDaoImpl extends BaseDaoImpl implements SalaryCalculateItemDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.SalaryCalculateItemDao";
	}
}
