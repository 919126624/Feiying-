package com.wuyizhiye.basedata.org.model;

import java.util.List;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.org.enums.WorkBenchTypeEnum;

/**
 * @ClassName Post
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class Post extends DataEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 职等
	 */
	private List<PostLevel> postLevels;
	
	/**
	 * 工作台类型
	 */
	private WorkBenchTypeEnum workBenchType;
	
	/**
	 * 岗位大类
	 */
	private PostCategory postCategory;


	public List<PostLevel> getPostLevels() {
		return postLevels;
	}

	public void setPostLevels(List<PostLevel> postLevels) {
		this.postLevels = postLevels;
	}

	public PostCategory getPostCategory() {
		return postCategory;
	}

	public void setPostCategory(PostCategory postCategory) {
		this.postCategory = postCategory;
	}

	public void setWorkBenchType(WorkBenchTypeEnum workBenchType) {
		this.workBenchType = workBenchType;
	}

	public WorkBenchTypeEnum getWorkBenchType() {
		return workBenchType;
	}
	
}
