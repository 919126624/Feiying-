package com.wuyizhiye.basedata.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.code.model.PrintConfig;
import com.wuyizhiye.basedata.code.service.PrintConfigService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PrintConfigEditController
 * @Description 打印配置
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/printConfig/*")
public class PrintConfigEditController extends EditController {

	@Autowired
	private PrintConfigService printConfigService;
	@Override
	protected Class getSubmitClass() {
		return PrintConfig.class;
	}

	@Override
	protected BaseService getService() {
		return printConfigService;
	}

}
