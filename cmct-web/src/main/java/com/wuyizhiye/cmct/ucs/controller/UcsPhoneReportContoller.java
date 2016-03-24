package com.wuyizhiye.cmct.ucs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneNumber;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName UcsPhoneReportContoller
 * @Description ucs话务通话记录
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/ucsPhoneReport/*")
public class UcsPhoneReportContoller extends ListController {

	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return new UcsPhoneNumber();
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "cmct/ucs/ucsPhoneReportList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return "cmct/ucs/ucsPhoneReportList";
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}

}
