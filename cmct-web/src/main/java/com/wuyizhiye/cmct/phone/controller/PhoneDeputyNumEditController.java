package com.wuyizhiye.cmct.phone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneDeputyNum;
import com.wuyizhiye.cmct.phone.service.PhoneDeputyNumService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PhoneDeputyNumEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneDeputyNum/*")
public class PhoneDeputyNumEditController extends EditController {

	@Autowired
	private PhoneDeputyNumService phoneDeputyNumService;
	
	@Override
	protected Class getSubmitClass() {
		return PhoneDeputyNum.class;
	}

	@Override
	protected BaseService getService() {
		return phoneDeputyNumService;
	}

}
