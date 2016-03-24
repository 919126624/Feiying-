package com.wuyizhiye.hr.affair.service;

import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.affair.model.EmployeeRunDiskBill;

/**
 * 
 * @author ouyangyi
 * @since 2013-4-23 下午03:18:10
 */
public interface EmployeeRunDiskService extends BaseService<EmployeeRunDiskBill> {
	
	/**
	 * 跑盘员入职申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	public String approveRunDisk(String[] billIds ,String approveType);
	
	/**
	 * 跑盘员入职申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveBill(String billId );
	
	/**
	 * 主要职位调动 定时器  写任职历史和修改人员职位信息
	 * @param billId
	 */
	@Transactional
	public void approveSchedule(String billId)throws Exception;
	
}
