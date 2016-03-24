package com.wuyizhiye.basedata.remind.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.remind.model.Remind;
import com.wuyizhiye.basedata.remind.service.RemindService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName RemindEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/remind/*")
public class RemindEditController extends EditController{
	
	@Autowired
	private RemindService remindService;

	@Override
	protected Class<Remind> getSubmitClass() {
		return Remind.class;
	}

	@Override
	protected BaseService<Remind> getService() {
		return remindService;
	}

}
