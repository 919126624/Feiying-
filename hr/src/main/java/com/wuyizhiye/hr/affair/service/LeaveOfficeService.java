/**
 * com.wuyizhiye.hr.affair.service.LeaveOfficeService.java
 */
package com.wuyizhiye.hr.affair.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.hr.affair.model.LeaveOffice;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;

/**
 * @author Cai.xing
 * @since 2013-04-08
 */
public interface LeaveOfficeService extends BaseService<LeaveOffice> {
	
	/**
	 * 审核
	 * @param billId
	 * @return
	 */
	public Map<String,Object> approvalBill(String billId );
	
	/**
	 * 人员离职 定时器  写任职历史和修改人员信息
	 * @param billId
	 */
	@Transactional
	public void approveSchedule(String billId)throws Exception;
	
	public void leaveOffice(Person per,PositionHistoryBill bill);
}
