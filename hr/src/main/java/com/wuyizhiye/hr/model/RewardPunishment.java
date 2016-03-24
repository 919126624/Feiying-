package com.wuyizhiye.hr.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.basic.model.BasicData;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.hr.enums.RewardPunishmentEnum;

/**
 * 奖惩记录
 * @author taking.wang
 * @since 2013-01-15
 *
 */
public class RewardPunishment extends CoreEntity {

	/**
	 * 人员
	 */
	private Person person;
	/**
	 * 奖或惩
	 */
	private RewardPunishmentEnum rewardPunishment;
	/**
	 * 奖惩类型
	 */
	private BasicData type;
	/**
	 * 奖惩级别
	 */
	private BasicData level;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 奖惩日期
	 */
	private Date date;
	/**
	 * 奖惩措施
	 */
	private String measure;
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public RewardPunishmentEnum getRewardPunishment() {
		return rewardPunishment;
	}
	public void setRewardPunishment(RewardPunishmentEnum rewardPunishment) {
		this.rewardPunishment = rewardPunishment;
	}
	public BasicData getType() {
		return type;
	}
	public void setType(BasicData type) {
		this.type = type;
	}
	public BasicData getLevel() {
		return level;
	}
	public void setLevel(BasicData level) {
		this.level = level;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMeasure() {
		return measure;
	}
	public void setMeasure(String measure) {
		this.measure = measure;
	}
}
