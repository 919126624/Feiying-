
package com.wuyizhiye.hr.attendance.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.attendance.dao.AttendanceDetailDao;


/**
 * 考勤明细
 * @author ouyangyi
 * @since 2013-5-17 下午02:53:01
 */
@Component(value="attendanceDetailDao")
public class AttendanceDetailDaoImpl extends BaseDaoImpl implements AttendanceDetailDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.attendance.dao.AttendanceDetailDao";
	}
}
