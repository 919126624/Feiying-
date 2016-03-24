package com.wuyizhiye.basedata.portlet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.portlet.model.QuickSet;
import com.wuyizhiye.basedata.portlet.service.QuickSetService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName QuickSetEditController
 * @Description 快捷设置
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping("interflow/quickSet/*")
public class QuickSetEditController extends EditController{
	@Autowired
	private QuickSetService quickSetService;
	

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getSubmitClass() {
		// TODO Auto-generated method stub
		return QuickSet.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return quickSetService;
	}

}
