package com.wuyizhiye.basedata.notetemplate.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.notetemplate.model.NoteTemplate;
import com.wuyizhiye.basedata.notetemplate.model.NoteTemplateEntry;
import com.wuyizhiye.basedata.notetemplate.service.NoteTemplateService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName NoteTemplateEditController
 * @Description 票据模板
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/notetemplate/*")
public class NoteTemplateEditController extends EditController {

	@Autowired
	private NoteTemplateService noteTemplateService;
	
	@Override
	protected Class getSubmitClass() {
		// TODO Auto-generated method stub
		return NoteTemplate.class;
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return noteTemplateService;
	}
	
	@Override
	protected NoteTemplate getSubmitEntity(){
		NoteTemplate nt =(NoteTemplate)super.getSubmitEntity();
		String column = getString("columnJson");
		List<NoteTemplateEntry> entryList = new ArrayList<NoteTemplateEntry>();
		if(!StringUtils.isEmpty(column)){
			JSONArray ja = JSONArray.fromObject(column);
			Collection<NoteTemplateEntry> filters = JSONArray.toCollection(ja, NoteTemplateEntry.class);
			if(filters != null){
				for(NoteTemplateEntry c : filters){
					entryList.add(c);
				}
			}
		}
		nt.setEntrylist(entryList);
		return nt;
	}
	
	@Override
	protected boolean validate(Object data) {
		NoteTemplate scheme = (NoteTemplate)data;
		Map<String,Object> param = new HashMap<String, Object>();
		if(StringUtils.isEmpty(scheme.getName()) || StringUtils.isEmpty(scheme.getNumber())){
			getOutputMsg().put("MSG", "名称和编码不能为空");
			return false;
		}
		List<NoteTemplate> list = null;
		param.put("noeqid", scheme.getId());
		param.put("validate", "validate");
		param.put("name", scheme.getName());
		param.put("number", scheme.getNumber());
		list = queryExecutor.execQuery("com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateDao.select", param, NoteTemplate.class);
		if(list != null && list.size() > 0){		
			getOutputMsg().put("MSG", "名称或编码己存在");
			return false;

		}

		return super.validate(data);
	}
}
