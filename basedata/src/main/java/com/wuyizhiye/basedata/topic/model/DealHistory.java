package com.wuyizhiye.basedata.topic.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.topic.enums.TopicStatusEnum;

/**
 * @ClassName DealHistory
 * @Description 话题处理(回复)历史
 * @author li.biao
 * @date 2015-4-3
 */
public class DealHistory extends CoreEntity {
	private static final long serialVersionUID = -7457479630648545424L;
	
	/**
	 * 意见(回复)
	 */
	private String remark;
	
	/**
	 * 处理(回复)时间
	 */
	private Date dealTime;
	
	/**
	 * 处理(回复)人
	 */
	private Person dealPerson;
	
	/**
	 * 处理记录
	 */
	private String record;
	
	/**
	 * 话题
	 */
	private Topic topic;
	
	/**
	 * 处理状态
	 */
	private TopicStatusEnum status;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public Person getDealPerson() {
		return dealPerson;
	}

	public void setDealPerson(Person dealPerson) {
		this.dealPerson = dealPerson;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setStatus(TopicStatusEnum status) {
		this.status = status;
	}

	public TopicStatusEnum getStatus() {
		return status;
	}
}
