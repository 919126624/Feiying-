/**
 * com.wuyizhiye.hr.attendance.service.LeaveRuleService.java
 */
package com.wuyizhiye.hr.attendance.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.attendance.model.LeaveRule;

/**
 * @author FengMy
 * 
 * @since 2013-11-21
 */
public interface LeaveRuleService extends BaseService<LeaveRule> {
	LeaveRule getEntityByOrgId(String id);
}
