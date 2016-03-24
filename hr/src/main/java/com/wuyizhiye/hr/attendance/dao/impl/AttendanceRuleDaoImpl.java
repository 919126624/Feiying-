package com.wuyizhiye.hr.attendance.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.attendance.dao.AttendanceRuleDao;


/**
 * 考勤规则
 * @author ouyangyi
 * @since 2013-11-20 上午10:19:38
 */
@Component(value="attendanceRuleDao")
public class AttendanceRuleDaoImpl extends BaseDaoImpl implements AttendanceRuleDao {
	@Override
	protected String getNameSpace(){
		return "com.wuyizhiye.hr.attendance.dao.AttendanceRuleDao";
	}
}
