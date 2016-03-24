package com.wuyizhiye.basedata.org.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.JobEtc;
import com.wuyizhiye.basedata.org.service.JobEtcService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName JobEtcEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/jobEtc/*")
public class JobEtcEditController extends EditController {

	@Autowired
	private JobEtcService jobEtcService;
	
	@Override
	protected Class getSubmitClass() {
		return JobEtc.class;
	}

	@Override
	protected BaseService getService() {
		return jobEtcService;
	}
}
