package com.wuyizhiye.basedata.notetemplate.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateEntryDao;

/**
 * @ClassName NoteTemplateEntryDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="noteTemplateEntryDao")
public class NoteTemplateEntryDaoImpl extends BaseDaoImpl implements NoteTemplateEntryDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateEntryDao";
	}
}
