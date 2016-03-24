package com.wuyizhiye.cmct.phone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneCosDetail;
import com.wuyizhiye.cmct.phone.service.PhoneCosDetailService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PhoneCosDetailEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneCosDetail/*")
public class PhoneCosDetailEditController extends EditController {

	@Autowired
	private PhoneCosDetailService phoneCosDetailService;
	@Override
	protected Class getSubmitClass() {
		// TODO Auto-generated method stub
		return PhoneCosDetail.class;
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return phoneCosDetailService;
	}

}
