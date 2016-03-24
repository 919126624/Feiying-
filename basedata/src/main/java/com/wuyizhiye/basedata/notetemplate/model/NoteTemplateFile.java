package com.wuyizhiye.basedata.notetemplate.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName NoteTemplateFile
 * @Description 模板文件
 * @author li.biao
 * @date 2015-4-2
 */
public class NoteTemplateFile extends CoreEntity {
	
	private String name;
	private String bgphoto;
	private String tmpfile;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBgphoto() {
		return bgphoto;
	}
	public void setBgphoto(String bgphoto) {
		this.bgphoto = bgphoto;
	}
	public String getTmpfile() {
		return tmpfile;
	}
	public void setTmpfile(String tmpfile) {
		this.tmpfile = tmpfile;
	}
}
