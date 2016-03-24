package com.wuyizhiye.basedata.notetemplate.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateDao;

/**
 * @ClassName NoteTemplateDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="noteTemplateDao")
public class NoteTemplateDaoImpl extends BaseDaoImpl implements NoteTemplateDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.notetemplate.dao.NoteTemplateDao";
	}
}
