package com.wuyizhiye.hr.attendance.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.attendance.model.AttendanceRule;


/**
 * 考勤规则
 * @author ouyangyi
 * @since 2013-11-20 上午10:22:58
 */
public interface AttendanceRuleService extends BaseService<AttendanceRule> {
	
	
	/**
	 * 定点执行  考勤录入提醒
	 */
	void execute(); 
}
