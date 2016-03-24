package com.wuyizhiye.hr.affair.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.affair.dao.EmployeeRunDiskDao;

/**
 * 
 * @author ouyangyi
 * @since 2013-4-23 下午03:12:23
 */
@Component(value="employeeRunDiskDao")
public class EmployeeRunDiskDaoImpl extends BaseDaoImpl implements EmployeeRunDiskDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.affair.dao.EmployeeRunDiskDao";
	}
}
