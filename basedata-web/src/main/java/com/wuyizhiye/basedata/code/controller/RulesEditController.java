package com.wuyizhiye.basedata.code.controller;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.code.model.RuleItems;
import com.wuyizhiye.basedata.code.model.Rules;
import com.wuyizhiye.basedata.code.service.RulesService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName RulesEditController
 * @Description 自定义编号规则
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/code/*")
public class RulesEditController extends EditController {
	
	@Autowired
	private RulesService rulesService;
	
	@Override
	protected Class getSubmitClass() {
		return Rules.class;
	}

	@Override
	protected BaseService getService() {
		return rulesService;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Object getSubmitEntity() {
		Rules rules = (Rules) super.getSubmitEntity();
		String entityJson = getString("entityJson");
		if(!StringUtils.isEmpty(entityJson)){
			Collection<RuleItems> tmp = JSONArray.toCollection(JSONArray.fromObject(entityJson), RuleItems.class);
			rules.setRuleItems(new ArrayList<RuleItems>(tmp));
		}
		return rules;
	}
	
}
