/**
 * com.wuyizhiye.hr.attendance.dao.impl.ScheduleRuleDaoImpl.java
 */
package com.wuyizhiye.hr.attendance.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.attendance.dao.ScheduleRuleDao;

/**
 * @author FengMy
 * 
 * @since 2013-11-25
 */
@Component(value="scheduleRuleDao")
public class ScheduleRuleDaoImpl extends BaseDaoImpl implements ScheduleRuleDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.attendance.dao.ScheduleRuleDao";
	}
}
