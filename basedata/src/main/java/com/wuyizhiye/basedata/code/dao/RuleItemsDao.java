package com.wuyizhiye.basedata.code.dao;

import java.util.List;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.code.model.RuleItems;

/**
 * @ClassName RuleItemsDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface RuleItemsDao extends BaseDao {
	
	/**
	 * 根据规则主表ID,删除明细信息
	 * @param id
	 */
	void deleteByRule(String id);
	
	
	/**
	 * 根据规则主表ID,查找该规则的明细规则
	 * @param ruleId  
	 * @return
	 */
	List<RuleItems> getByRuleItems(String ruleId);
}
