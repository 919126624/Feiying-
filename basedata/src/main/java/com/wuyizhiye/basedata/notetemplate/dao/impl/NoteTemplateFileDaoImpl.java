package com.wuyizhiye.basedata.notetemplate.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateFileDao;

/**
 * @ClassName NoteTemplateFileDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="noteTemplateFileDao")
public class NoteTemplateFileDaoImpl extends BaseDaoImpl implements NoteTemplateFileDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateFileDao";
	}
}
