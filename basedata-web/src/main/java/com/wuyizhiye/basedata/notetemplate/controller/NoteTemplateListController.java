package com.wuyizhiye.basedata.notetemplate.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.notetemplate.model.NoteTemplate;
import com.wuyizhiye.basedata.notetemplate.service.NoteTemplateService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName NoteTemplateListController
 * @Description 票据模板
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/notetemplate/*")
public class NoteTemplateListController extends ListController {

	@Autowired
	private NoteTemplateService noteTemplateService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		
		return "basedata/notetemplate/noteTemplateList";
	}

	@Override
	protected String getEditView() {
		String moduleId = this.getString("moduleId");
		this.getRequest().setAttribute("moduleId", moduleId);
		return "basedata/notetemplate/noteTemplateEdit";
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return noteTemplateService;
	}
	
	@RequestMapping(value="printdemo")
	protected String printdemo(ModelMap m) {
		Map param = new HashMap();
		String templateId = this.getString("templateId");
		if(StringUtils.isEmpty(templateId)) throw new RuntimeException("123");
		param.put("templateId", templateId);
		NoteTemplate nt = 
		this.queryExecutor.execOneEntity("com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateDao.selectJoin", param, NoteTemplate.class);
		
		m.put("buildtemplate", this.noteTemplateService.printFileTemplate(this.getParaMap(), "login.ftl"));
		return "basedata/notetemplate/printdemo";
		
	}
	

}
