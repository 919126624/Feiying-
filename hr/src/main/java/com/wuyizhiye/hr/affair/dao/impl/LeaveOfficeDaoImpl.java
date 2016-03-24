/**
 * com.wuyizhiye.hr.affair.dao.impl.LeaveOfficeDaoImpl.java
 */
package com.wuyizhiye.hr.affair.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.affair.dao.LeaveOfficeDao;

/**
 * @author Cai.xing
 * @since 2013-04-08
 */
@Component(value="leaveOfficeDao")
public class LeaveOfficeDaoImpl extends BaseDaoImpl implements LeaveOfficeDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.affair.dao.LeaveOfficeDao";
	}
}
