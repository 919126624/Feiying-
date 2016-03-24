/**
 * com.wuyizhiye.hr.salary.dao.impl.SalaryItemDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.SalaryItemDao;

/**
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="salaryItemDao")
public class SalaryItemDaoImpl extends BaseDaoImpl implements SalaryItemDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.SalaryItemDao";
	}
}
