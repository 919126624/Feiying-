/**
 * com.wuyizhiye.hr.attendance.service.ClearanceLeaveService.java
 */
package com.wuyizhiye.hr.attendance.service;

import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.attendance.model.ClearanceLeave;

/**
 * @author FengMy
 * 
 * @since 2013-11-20
 */
public interface ClearanceLeaveService extends BaseService<ClearanceLeave> {
	/**
	 * 销假申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	public String approveClearanceLeave(String[] billIds ,String approveType);
	
	/**
	 * 销假申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveClearanceLeave(String billId );	
}
