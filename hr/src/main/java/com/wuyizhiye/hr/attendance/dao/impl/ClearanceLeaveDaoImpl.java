/**
 * com.wuyizhiye.hr.attendance.dao.impl.ClearanceLeaveDaoImpl.java
 */
package com.wuyizhiye.hr.attendance.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.attendance.dao.ClearanceLeaveDao;

/**
 * @author FengMy
 * 
 * @since 2013-11-20
 */
@Component(value="clearanceLeaveDao")
public class ClearanceLeaveDaoImpl extends BaseDaoImpl implements ClearanceLeaveDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.attendance.dao.ClearanceLeaveDao";
	}
}
