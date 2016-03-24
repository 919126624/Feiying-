/**
 * com.wuyizhiye.hr.dao.impl.EmployeeOrientationDaoImpl.java
 */
package com.wuyizhiye.hr.affair.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.affair.dao.EmployeeOrientationDao;

/**
 * @author Cai.cing
 * 
 * @since 2013-04-02
 */
@Component(value="employeeOrientationDao")
public class EmployeeOrientationDaoImpl extends BaseDaoImpl implements EmployeeOrientationDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.affair.dao.EmployeeOrientationDao";
	}
}
