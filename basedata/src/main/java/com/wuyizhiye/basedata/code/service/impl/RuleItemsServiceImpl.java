package com.wuyizhiye.basedata.code.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.code.dao.RuleItemsDao;
import com.wuyizhiye.basedata.code.model.RuleItems;
import com.wuyizhiye.basedata.code.service.RuleItemsService;


/**
 * @ClassName RuleItemsServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value = "ruleItemsService")
public class RuleItemsServiceImpl extends BaseServiceImpl<RuleItems> implements
		RuleItemsService {
	@Autowired
	private RuleItemsDao ruleItemsDao;

	@Override
	protected BaseDao getDao() {
		return ruleItemsDao;
	}

	@Override
	public void deleteByRule(String id) {
		ruleItemsDao.deleteByRule(id);
	}

}