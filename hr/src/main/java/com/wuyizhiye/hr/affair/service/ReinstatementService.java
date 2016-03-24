/**
 * com.wuyizhiye.hr.affair.service.ReinstatementService.java
 */
package com.wuyizhiye.hr.affair.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.affair.model.Reinstatement;

/**
 * @author Cai.xing
 * @since 2013-04-08
 */
public interface ReinstatementService extends BaseService<Reinstatement> {
	/**
	 * 审批通过调用方法
	 * @author Cai.xing
	 * */
	public Map<String,Object> approveBill(String billId );
	
	/**
	 * 人员复职 定时器  写任职历史和修改人员信息
	 * @param billId
	 */
	@Transactional
	public void approveSchedule(String billId);
}
