/**
 * com.wuyizhiye.hr.attendance.service.LeaveService.java
 */
package com.wuyizhiye.hr.attendance.service;

import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.attendance.model.Leave;

/**
 * @author FengMy
 * 
 * @since 2013-11-20
 */
public interface LeaveService extends BaseService<Leave> {
	/**
	 * 请假申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	public String approveAsk4Leave(String[] billIds ,String approveType);
	
	/**
	 * 请假申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveAsk4Leave(String billId );
}
