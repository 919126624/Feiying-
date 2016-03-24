package com.wuyizhiye.basedata.exporttools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.exporttools.service.ExportColumnService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName ExportColumnListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="exporttools/exportColumn/*")
public class ExportColumnListController extends ListController {
	@Autowired
	private ExportColumnService exportColumnService;
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		return null;
	}

	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.exporttools.dao.ExportColumnDao.select";
	}

	@Override
	protected ExportColumnService getService() {
		return exportColumnService;
	}

}
