package com.wuyizhiye.basedata.notetemplate.model;

import java.util.List;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName NoteTemplate
 * @Description 票据模板
 * @author li.biao
 * @date 2015-4-2
 */
public class NoteTemplate extends DataEntity {
	

	private NoteTemplateFile url;
	
	/**
	 * 所属模块
	 */
	private String moduleId;
	
	private List<NoteTemplateEntry> entrylist;


	public NoteTemplateFile getUrl() {
		return url;
	}

	public void setUrl(NoteTemplateFile url) {
		this.url = url;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public List<NoteTemplateEntry> getEntrylist() {
		return entrylist;
	}

	public void setEntrylist(List<NoteTemplateEntry> entrylist) {
		this.entrylist = entrylist;
	}
	
}
