package com.wuyizhiye.basedata.notetemplate.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateDao;
import com.wuyizhiye.basedata.notetemplate.model.NoteTemplate;
import com.wuyizhiye.basedata.notetemplate.model.NoteTemplateEntry;
import com.wuyizhiye.basedata.notetemplate.service.NoteTemplateEntryService;
import com.wuyizhiye.basedata.notetemplate.service.NoteTemplateService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.FreeMarkUtil;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @ClassName NoteTemplateServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="noteTemplateService")
@Transactional
public class NoteTemplateServiceImpl extends DataEntityService<NoteTemplate> implements NoteTemplateService {
	private Logger logger=Logger.getLogger(NoteTemplateServiceImpl.class);
	@Autowired
	private NoteTemplateDao noteTemplateDao;
	@Autowired
	private NoteTemplateEntryService entryService;
	@Override
	protected BaseDao getDao() {
		return noteTemplateDao;
	}	
	
	@Override
	public void addEntity(NoteTemplate entity) {
		entity.setId(UUID.randomUUID().toString());
		super.addEntity(entity);
		List<NoteTemplateEntry> entrylist = entity.getEntrylist();
		if(null!=entrylist){
			int index = 0;
			for(NoteTemplateEntry n : entrylist){
				n.setParentId(entity.getId());
				n.setIndex(index++);
				n.setUUID();
			}
			this.entryService.addBatch(entrylist);
		}
	}
	
	@Override
	public void updateEntity(NoteTemplate entity){
		super.updateEntity(entity);
		this.entryService.deleteById(entity.getId());
		List<NoteTemplateEntry> entrylist = entity.getEntrylist();
		if(null!=entrylist){
			int index = 0;
			for(NoteTemplateEntry n : entrylist){
				n.setParentId(entity.getId());
				n.setIndex(index++);
				n.setUUID();
			}
			this.entryService.addBatch(entrylist);
		}
	}


	@Override
	public String printTemplate(Map<String, Object> map) {
		Configuration cfg=new Configuration();
		cfg.setDefaultEncoding("UTF-8");	
		StringTemplateLoader stringLoader=new StringTemplateLoader();
		stringLoader.putTemplate("111A", "123");
		cfg.setTemplateLoader(stringLoader);
		Template template;
		try {
			template = cfg.getTemplate("111A");
			StringWriter sw=new StringWriter();
			template.process(map, sw);
			return sw.toString();
		} catch (IOException e) {
			logger.error("", e);
		} catch (TemplateException e) {
			logger.error("", e);
		}
		return null;

	}

	@Override
	public String printFileTemplate(Map<String, Object> map,String path) {
	
		Template template;
		try {
			template = FreeMarkUtil.TemplateFactory(path);
			StringWriter sw=new StringWriter();
			template.process(map, sw);
			return sw.toString();
		} catch (IOException e) {
			logger.error("", e);
		} catch (TemplateException e) {
			logger.error("", e);
		}
		return null;
	}
}