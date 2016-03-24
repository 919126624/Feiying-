package com.wuyizhiye.basedata.topic.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.topic.enums.TopicLevelEnum;
import com.wuyizhiye.basedata.topic.enums.TopicStatusEnum;
import com.wuyizhiye.basedata.topic.enums.TopicTypeEnum;

/**
 * @ClassName Topic
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class Topic extends DataEntity {
	private static final long serialVersionUID = -410045307174865906L;
	/**
	 * 所属功能
	 */
	private Menu belongFunction;
	
	/**
	 * 话题类型
	 */
	private TopicTypeEnum type;
	
	/**
	 * 话题级别
	 */
	private TopicLevelEnum level;
	
	/**
	 * 步骤
	 */
	private String step;
	
	/**
	 * 状态
	 */
	private TopicStatusEnum status;
	
	/**
	 * 当前处理人
	 */
	private Person dealPerson;
	
	/**
	 * 提交时间
	 */
	private Date submitTime;
	
	/**
	 * 处理时间
	 */
	private Date dealTime;
	
	/**
	 * 关闭时间
	 */
	private Date closeTime;
	
	/**
	 * 常见问题
	 */
	private Integer saq; 
	
	/**
	 * 数据类型,标示为新版以及老版,OLD,NEW 
	 */
	private String dataType; 

	/**
	 * 是否有图片  , 用于展现,不做持久化
	 */
	private boolean  hasPhoto = false; 
	
	/**
	 * 评论数,用于展现
	 */
	private int commentCount; 
	
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public boolean isHasPhoto() {
		return hasPhoto;
	}

	public void setHasPhoto(boolean hasPhoto) {
		this.hasPhoto = hasPhoto;
	}

	private Boolean hasNotDealed;

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public Menu getBelongFunction() {
		return belongFunction;
	}

	public void setBelongFunction(Menu belongFunction) {
		this.belongFunction = belongFunction;
	}

	public TopicTypeEnum getType() {
		return type;
	}

	public void setType(TopicTypeEnum type) {
		this.type = type;
	}

	public TopicLevelEnum getLevel() {
		return level;
	}

	public void setLevel(TopicLevelEnum level) {
		this.level = level;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public TopicStatusEnum getStatus() {
		return status;
	}

	public void setStatus(TopicStatusEnum status) {
		this.status = status;
	}

	public void setDealPerson(Person dealPerson) {
		this.dealPerson = dealPerson;
	}

	public Person getDealPerson() {
		return dealPerson;
	}

	public Boolean getHasNotDealed() {
		return hasNotDealed;
	}

	public void setHasNotDealed(Boolean hasNotDealed) {
		this.hasNotDealed = hasNotDealed;
	}
	
	public Integer getSaq() {
		return saq;
	}

	public void setSaq(Integer saq) {
		this.saq = saq;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getCommentCount() {
		return commentCount;
	}
}
