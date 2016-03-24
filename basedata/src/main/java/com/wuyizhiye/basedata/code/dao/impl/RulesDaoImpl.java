package com.wuyizhiye.basedata.code.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.code.dao.RulesDao;
import com.wuyizhiye.basedata.code.model.Rules;

/**
 * @ClassName RulesDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="rulesDao")
public class RulesDaoImpl extends BaseDaoImpl implements  RulesDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.code.RulesDao";
	}

	@Override
	public Rules getRulesByOrg(String orgId) {
		Map<String,Object>  param = new HashMap<String,Object>();
		param.put("orgId", orgId);
		return (Rules)getSqlSession().selectOne(getNameSpace()+".selectByOrg", param);
	}

	@Override
	public void updateStatus(Rules rules) {
		getSqlSession().update(getNameSpace()+".updateStatus", rules);
	}
}
