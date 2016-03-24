package com.wuyizhiye.basedata.permission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.permission.model.Shortcuts;
import com.wuyizhiye.basedata.permission.service.ShortcutsService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName ShortcutsListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="permission/shortcuts/*")
public class ShortcutsListController extends ListController {
	@Autowired
	private ShortcutsService shortcutsService;
	@Override
	protected CoreEntity createNewEntity() {
		return new Shortcuts();
	}

	@Override
	protected String getListView() {
		return "";
	}

	@Override
	protected String getEditView() {
		return "";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.permission.dao.ShortcutsDao.select";
	}

	@Override
	protected BaseService<Shortcuts> getService() {
		return shortcutsService;
	}
	
}
