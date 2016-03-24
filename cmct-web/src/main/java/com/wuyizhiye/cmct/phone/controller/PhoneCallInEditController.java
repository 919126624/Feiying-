package com.wuyizhiye.cmct.phone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneCallIn;
import com.wuyizhiye.cmct.phone.service.PhoneCallInService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PhoneCallInEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneCallIn/*")
public class PhoneCallInEditController extends EditController {

	@Autowired
	private PhoneCallInService phoneCallInService;
	
	@Override
	protected Class getSubmitClass() {
		return PhoneCallIn.class;
	}

	@Override
	protected BaseService getService() {
		return phoneCallInService;
	}

}
