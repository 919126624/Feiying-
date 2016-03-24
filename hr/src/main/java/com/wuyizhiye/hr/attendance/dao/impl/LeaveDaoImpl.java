/**
 * com.wuyizhiye.hr.attendance.dao.impl.LeaveDaoImpl.java
 */
package com.wuyizhiye.hr.attendance.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.attendance.dao.LeaveDao;

/**
 * @author FengMy
 * 
 * @since 2013-11-20
 */
@Component(value="leaveDao")
public class LeaveDaoImpl extends BaseDaoImpl implements LeaveDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.attendance.dao.LeaveDao";
	}
}
