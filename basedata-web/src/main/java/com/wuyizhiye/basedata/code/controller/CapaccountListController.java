package com.wuyizhiye.basedata.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.bank.enums.AccountTypeEnum;
import com.wuyizhiye.basedata.bank.model.Capaccount;
import com.wuyizhiye.basedata.bank.service.CapaccountService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName CapaccountListController
 * @Description 编号规则列表controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "trade/account/*")
public class CapaccountListController extends ListController {

	@Autowired
	private CapaccountService capaccountService;

	@Override
	protected CoreEntity createNewEntity() {
		return new Capaccount();
	}

	@Override
	protected String getListView() {
		return "trade/bank/capaccountList";
	}

	@Override
	protected String getEditView() {
		getRequest().setAttribute("accountTypes", AccountTypeEnum.values());
		return "trade/bank/capaccountEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.bank.CapaccountDao.select";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		return capaccountService;
	}
	
}
