package com.wuyizhiye.basedata.notetemplate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName NoteTemplateEntryController
 * @Description 票据模板分录
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/notetemplatentry/*")
public class NoteTemplateEntryController extends ListController {

	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateEntryDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}

}
