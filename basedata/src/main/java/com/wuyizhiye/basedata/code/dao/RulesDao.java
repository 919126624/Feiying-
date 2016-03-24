package com.wuyizhiye.basedata.code.dao;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.code.model.Rules;

/**
 * @ClassName RulesDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface RulesDao extends BaseDao {
	/**
	 * 根据组织查找该组织的编号生成规则
	 * @return
	 */
	Rules getRulesByOrg(String orgId);
	
	/**
	 * 修改规则的状态
	 * @param rules
	 */
	void updateStatus(Rules rules);
}
