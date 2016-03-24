package com.wuyizhiye.basedata.notetemplate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateEntryDao;
import com.wuyizhiye.basedata.notetemplate.model.NoteTemplateEntry;
import com.wuyizhiye.basedata.notetemplate.service.NoteTemplateEntryService;

/**
 * @ClassName NoteTemplateEntryServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="noteTemplateEntryService")
@Transactional
public class NoteTemplateEntryServiceImpl extends BaseServiceImpl<NoteTemplateEntry> implements NoteTemplateEntryService {
	@Autowired
	private NoteTemplateEntryDao noteTemplateEntryDao;
	@Override
	protected BaseDao getDao() {
		return noteTemplateEntryDao;
	}	
	

}