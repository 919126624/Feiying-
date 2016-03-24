package com.wuyizhiye.basedata.sync.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName BillTypeCopy
 * @Description 单据类型实体拷贝
 * @author li.biao
 * @date 2015-4-3
 */
public class BillTypeCopy extends CoreEntity {
	
	private String name;
	private String description;
	private String number;
	private String module;
	private String addLink;
	private String editLink;
	private String viewLink;
	private String processDefinitionId;
	private String viewSize;
	private String editSize;
	private String addSize;
	private String deleteLink;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getAddLink() {
		return addLink;
	}
	public void setAddLink(String addLink) {
		this.addLink = addLink;
	}
	public String getEditLink() {
		return editLink;
	}
	public void setEditLink(String editLink) {
		this.editLink = editLink;
	}
	public String getViewLink() {
		return viewLink;
	}
	public void setViewLink(String viewLink) {
		this.viewLink = viewLink;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getViewSize() {
		return viewSize;
	}
	public void setViewSize(String viewSize) {
		this.viewSize = viewSize;
	}
	public String getEditSize() {
		return editSize;
	}
	public void setEditSize(String editSize) {
		this.editSize = editSize;
	}
	public String getAddSize() {
		return addSize;
	}
	public void setAddSize(String addSize) {
		this.addSize = addSize;
	}
	public String getDeleteLink() {
		return deleteLink;
	}
	public void setDeleteLink(String deleteLink) {
		this.deleteLink = deleteLink;
	}
}
