package com.wuyizhiye.hr.affair.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.hr.affair.model.ProcessInfo;

/**
 * 单据信息(公司动态)
 * @author hlz
 *
 */
public interface ProcessInfoService extends BaseService<ProcessInfo> {

	public void insertProcessInfo(PersonPositionHistory pph);
}
