/**
 * com.wuyizhiye.hr.affair.service.PositiveService.java
 */
package com.wuyizhiye.hr.affair.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.affair.model.Positive;

/**
 * @author Cai.xing
 * @since 2013-04-08
 */
public interface PositiveService extends BaseService<Positive> {
	
	/**
	 * 审批通过调用方法
	 * @author Cai.xing
	 * */
	@RequestMapping(value="approvalBill")
	public Map<String,Object> approvalBill(String billId);
	
	/**
	 * 人员转正 定时器  写任职历史和修改人员信息
	 * @param billId
	 */
	@Transactional
	public void approveSchedule(String billId);
}
