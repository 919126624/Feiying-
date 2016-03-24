package com.wuyizhiye.basedata.org.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName PostLevel
 * @Description 职级
 * @author li.biao
 * @date 2015-4-2
 */
public class PostLevel extends DataEntity {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 岗位
	 */
	private Post post;
	
	/**
	 * 级别
	 */
	private Integer level;

	
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getLevel() {
		return level;
	}
}
