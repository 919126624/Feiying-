package com.wuyizhiye.basedata.bank.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.bank.model.Capaccount;


/**
 * @ClassName CapaccountService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface CapaccountService extends BaseService<Capaccount> {
	/**
	 * 得到总部账户 不包括现金
	 * @return
	 */
	public List<Capaccount> getGroupCapaccountList();
	/**
	 * 得到总部账户包括现金
	 * @return
	 */
	public List<Capaccount> getGroupAccountList();

}
