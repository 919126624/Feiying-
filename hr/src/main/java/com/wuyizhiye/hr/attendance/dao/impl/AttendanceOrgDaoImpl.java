package com.wuyizhiye.hr.attendance.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.attendance.dao.AttendanceOrgDao;


/**
 * 考勤组织方案
 * @author ouyangyi
 * @since 2013-11-20 上午10:19:38
 */
@Component(value="attendanceOrgDao")
public class AttendanceOrgDaoImpl extends BaseDaoImpl implements AttendanceOrgDao {
	@Override
	protected String getNameSpace(){
		return "com.wuyizhiye.hr.attendance.dao.AttendanceOrgDao";
	}
}
