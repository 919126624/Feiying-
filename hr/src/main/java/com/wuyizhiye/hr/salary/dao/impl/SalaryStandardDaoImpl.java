/**
 * com.wuyizhiye.hr.salary.dao.impl.SalaryStandardDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.SalaryStandardDao;

/**
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="salaryStandardDao")
public class SalaryStandardDaoImpl extends BaseDaoImpl implements SalaryStandardDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.SalaryStandardDao";
	}
}
