package com.wuyizhiye.basedata.portlet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.portlet.service.PositionPortletService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PositionPortletListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="interflow/positionPortlet/*")
public class PositionPortletListController extends ListController {

	@Autowired
	private PositionPortletService positionPortletService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		return "interflow/portlet/PositionPortletList";
	}

	@Override
	protected String getEditView() {
		return "interflow/portlet/PositionPortletEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.portlet.dao.PositionPortletDao.select";
	}

	@Override
	protected BaseService getService() {
		return positionPortletService;
	}

}
