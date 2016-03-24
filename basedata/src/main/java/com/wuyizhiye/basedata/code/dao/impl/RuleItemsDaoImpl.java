package com.wuyizhiye.basedata.code.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.code.dao.RuleItemsDao;
import com.wuyizhiye.basedata.code.model.RuleItems;

/**
 * @ClassName RuleItemsDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value = "ruleItemsDao")
public class RuleItemsDaoImpl extends BaseDaoImpl implements RuleItemsDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.code.RuleItemsDao";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RuleItems> getByRuleItems(String ruleId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ruleId", ruleId);
		return getSqlSession().selectList(getNameSpace() + ".select", param);
	}

	@Override
	public void deleteByRule(String id) {
		getSqlSession().delete(getNameSpace() + ".deleteByRule", id);
	}
}