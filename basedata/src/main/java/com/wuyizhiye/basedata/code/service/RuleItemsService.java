package com.wuyizhiye.basedata.code.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.code.model.RuleItems;

/**
 * @ClassName RuleItemsService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface RuleItemsService extends BaseService<RuleItems> {
	/**
	 * 根据规则主表ID,删除明细信息
	 * @param id
	 */
	void deleteByRule(String id);
}
