package com.wuyizhiye.basedata.notetemplate.service;

import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.notetemplate.model.NoteTemplate;

/**
 * @ClassName NoteTemplateService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface NoteTemplateService extends BaseService<NoteTemplate> {
	/**
	 * 字符串模板
	 * @param map
	 * @return
	 */
	String printTemplate(Map<String,Object> map);
	/**
	 * 文件模板
	 * @param map
	 * @return
	 */
	String printFileTemplate(Map<String,Object> map,String path);
}
