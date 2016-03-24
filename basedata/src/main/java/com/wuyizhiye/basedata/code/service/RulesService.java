package com.wuyizhiye.basedata.code.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.code.model.Rules;

/**
 * @ClassName RulesService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface RulesService extends BaseService<Rules> {
	/**
	 * 修改规则的状态
	 * @param rules
	 */
	void updateStatus(Rules rules);
}
