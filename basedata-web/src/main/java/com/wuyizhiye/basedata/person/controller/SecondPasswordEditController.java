package com.wuyizhiye.basedata.person.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.person.model.SecondPassword;
import com.wuyizhiye.basedata.person.service.SecondPasswordService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName SecondPasswordEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/secondPassword/*")
public class SecondPasswordEditController extends EditController{

	@Autowired
	private SecondPasswordService secondPasswordService;
	
	@Override
	protected Class getSubmitClass() {
		// TODO Auto-generated method stub
		return SecondPassword.class;
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return secondPasswordService;
	}
	
	@Override
	protected boolean validate(Object data) {
		
		return true;
	}
	
}
