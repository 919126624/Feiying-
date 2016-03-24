package com.wuyizhiye.basedata.code.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.code.dao.RuleItemsDao;
import com.wuyizhiye.basedata.code.dao.RulesDao;
import com.wuyizhiye.basedata.code.model.RuleItems;
import com.wuyizhiye.basedata.code.model.Rules;
import com.wuyizhiye.basedata.code.service.RuleItemsService;
import com.wuyizhiye.basedata.code.service.RulesService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName RulesServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value = "rulesService")
public class RulesServiceImpl extends DataEntityService<Rules> implements
		RulesService {
	@Autowired
	private RulesDao rulesDao;

	@Autowired
	private RuleItemsService ruleItemsService;
	@Autowired
	private RuleItemsDao ruleItemsDao;

	@Override
	protected BaseDao getDao() {
		return rulesDao;
	}

	@Override
	public void addEntity(Rules entity) {
		if (StringUtils.isEmpty(entity.getIsDisable())) {
			entity.setIsDisable("Y");
		}
		super.addEntity(entity);
		List<RuleItems> ruleItemsList = entity.getRuleItems();
		for (RuleItems items : ruleItemsList) {
			items.setRules(entity);
			ruleItemsService.addEntity(items);
		}
	}

	@Override
	public void updateEntity(Rules entity) {
		List<RuleItems> oldRuleItems = ruleItemsDao.getByRuleItems(entity
				.getId());
		List<RuleItems> ruleItems = entity.getRuleItems();
		List<RuleItems> updateRuleItems = new ArrayList<RuleItems>(ruleItems);
		List<RuleItems> newRuleItems = new ArrayList<RuleItems>();
		for (RuleItems d : ruleItems) {
			d.setRules(entity);
			if (StringUtils.isEmpty(d.getId())) {
				d.setId(UUID.randomUUID().toString());
				newRuleItems.add(d);
				updateRuleItems.remove(d);
			} else {
				for (RuleItems o : oldRuleItems) {
					if (o.getId().equals(d.getId())) {
						oldRuleItems.remove(o);
						break;
					}
				}
			}
		}
		for (RuleItems o : oldRuleItems) {
			ruleItemsService.deleteById(o.getId());
		}
		for (RuleItems o : newRuleItems) {
			ruleItemsService.addEntity(o);
		}
		for (RuleItems o : updateRuleItems) {
			ruleItemsService.updateEntity(o);
		}
		super.updateEntity(entity);
	}

	@Override
	public void deleteEntity(Rules entity) {
		ruleItemsService.deleteByRule(entity.getId());
		super.deleteEntity(entity);
	}

	@Override
	public void deleteById(String id) {
		ruleItemsService.deleteById(id);
		super.deleteById(id);
	}

	@Override
	public void updateStatus(Rules rules) {
		rulesDao.updateStatus(rules);
	}
}
