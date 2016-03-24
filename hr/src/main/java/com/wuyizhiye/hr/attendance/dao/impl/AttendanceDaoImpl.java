package com.wuyizhiye.hr.attendance.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.attendance.dao.AttendanceDao;


/**
 * 考勤
 * @author ouyangyi
 * @since 2013-5-17 下午02:54:00
 */
@Component(value="attendanceDao")
public class AttendanceDaoImpl extends BaseDaoImpl implements AttendanceDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.attendance.dao.AttendanceDao";
	}
}
