package com.wuyizhiye.basedata.topic.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName TopicComment
 * @Description 问题反馈评论
 * @author li.biao
 * @date 2015-4-3
 */
public class TopicComment extends DataEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 评论内容
	 */
	private String content;  
	/**
	 * 问题反馈
	 */
	private Topic topic;
	/**
	 * 图片地址
	 */
	private String photoUrl;
	
	/**
	 * 格式化时间
	 */
	private String  formatDate;
	
	public String getFormatDate() {
		return formatDate;
	}
	public void setFormatDate(String formatDate) {
		this.formatDate = formatDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
}
