package com.wuyizhiye.base.solr;

import org.apache.solr.client.solrj.beans.Field;

/**
 * @ClassName BeanDemo
 * @Description Solr Bean范例 
 * @author li.biao
 * @date 2015-4-1
 */
public class BeanDemo {
	
	@Field(value="id")
	private String id;
	
	@Field(value="name")
	private String name;
	
	@Field(value="title")
	private String title;
	
	@Field(value="comments")
	private String comments;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
