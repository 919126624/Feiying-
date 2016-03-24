package com.wuyizhiye.basedata.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.bank.model.Capaccount;
import com.wuyizhiye.basedata.bank.service.CapaccountService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName CapaccountEditController
 * @Description 资金账户编辑controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="trade/account/*")
public class CapaccountEditController extends EditController {
	@Autowired
	private CapaccountService capaccountService;
	@Override
	protected Class<Capaccount> getSubmitClass() {
		return Capaccount.class;
	}
	@Override
	protected BaseService<Capaccount> getService() {
		return capaccountService;
	}
}
