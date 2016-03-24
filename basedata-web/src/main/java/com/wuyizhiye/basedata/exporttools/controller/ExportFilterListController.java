package com.wuyizhiye.basedata.exporttools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.exporttools.service.ExportFilterService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName ExportFilterListController
 * @Description 数据导出过滤条件Controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="exporttools/exportFilter/*")
public class ExportFilterListController extends ListController {
	@Autowired
	private ExportFilterService exportFilterService;
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
		return "com.wuyizhiye.basedata.exporttools.dao.ExportFilterDao.select";
	}

	@Override
	protected ExportFilterService getService() {
		return exportFilterService;
	}

}
