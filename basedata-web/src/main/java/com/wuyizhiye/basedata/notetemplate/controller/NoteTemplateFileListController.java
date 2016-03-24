package com.wuyizhiye.basedata.notetemplate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.notetemplate.service.NoteTemplateFileService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName NoteTemplateFileListController
 * @Description 模板文件处理器
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/notetemplatefile/*")
public class NoteTemplateFileListController extends ListController {

	@Autowired
	private NoteTemplateFileService noteTemplateFileService;
	
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "basedata/notetemplate/noteTemplateFileList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return "basedata/notetemplate/noteTemplateFileEdit";
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateFileDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return noteTemplateFileService;
	}

}
