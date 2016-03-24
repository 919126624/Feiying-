package com.wuyizhiye.cmct.phone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneCallRecorDing;
import com.wuyizhiye.cmct.phone.service.PhoneCallRecorDingService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PhoneCallRecorDingEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneCrd/*")
public class PhoneCallRecorDingEditController extends EditController {

	@Autowired
	private PhoneCallRecorDingService phoneCallRecorDingService;
	
	@Override
	protected Class getSubmitClass() {
		return PhoneCallRecorDing.class;
	}

	@Override
	protected BaseService getService() {
		return phoneCallRecorDingService;
	}
}
