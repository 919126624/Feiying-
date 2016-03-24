package com.wuyizhiye.basedata.notetemplate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateFileDao;
import com.wuyizhiye.basedata.notetemplate.model.NoteTemplateFile;
import com.wuyizhiye.basedata.notetemplate.service.NoteTemplateFileService;

/**
 * @ClassName NoteTemplateFileServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="noteTemplateFileService")
@Transactional
public class NoteTemplateFileServiceImpl extends BaseServiceImpl<NoteTemplateFile> implements NoteTemplateFileService {
	@Autowired
	private NoteTemplateFileDao noteTemplateFileDao;
	@Override
	protected BaseDao getDao() {
		return noteTemplateFileDao;
	}	
}