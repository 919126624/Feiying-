/**
 * com.wuyizhiye.hr.attendance.service.OverTimeService.java
 */
package com.wuyizhiye.hr.attendance.service;

import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.attendance.model.OverTime;

/**
 * @author hyl
 * 
 * @since 2013-11-28
 */
public interface OverTimeService extends BaseService<OverTime> {
	/**
	 * 加班申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	public String approveOverTime(String[] billIds ,String approveType);
	
	/**
	 * 加班申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveOverTime(String billId );
}
