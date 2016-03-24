package com.wuyizhiye.basedata.notetemplate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.notetemplate.model.NoteTemplateFile;
import com.wuyizhiye.basedata.notetemplate.service.NoteTemplateFileService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName NoteTemplateFileEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/notetemplatefile/*")
public class NoteTemplateFileEditController extends EditController {

	@Autowired
	private NoteTemplateFileService noteTemplateFileService;
	
	@Override
	protected Class getSubmitClass() {
		// TODO Auto-generated method stub
		return NoteTemplateFile.class;
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return noteTemplateFileService;
	}

}
