/**
 * com.wuyizhiye.hr.service.EmployeeOrientationService.java
 */
package com.wuyizhiye.hr.affair.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.affair.model.HrBillBase;

/**
 * 人事流程定时任务管理
 * @author 孙海涛
 * @since 2014-11-18
 */
public interface HRTaskService extends BaseService<HrBillBase> {
	 
	public void execute(String taskId);
}
