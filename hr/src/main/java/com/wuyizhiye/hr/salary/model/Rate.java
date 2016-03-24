package com.wuyizhiye.hr.salary.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * 税率表
 * @author hyl
 * @since 2014-02-12
 *
 */
public class Rate extends CoreEntity{
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 创建用户
	 */
	private Person creator;
	
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	
	/**
	 * 最后更新用户
	 */
	private Person updator;
	/**
	 * 起始点
	 */
	private Double startPoint;
	/**
	 * 终止点
	 */
	private Double endPoint;
	/**
	 * 运算扣除数
	 */
	private Double quickDeduction;
	/**
	 * 税率
	 */
	private Double rate;
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Person getCreator() {
		return creator;
	}
	public void setCreator(Person creator) {
		this.creator = creator;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Person getUpdator() {
		return updator;
	}
	public void setUpdator(Person updator) {
		this.updator = updator;
	}
	public Double getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(Double startPoint) {
		this.startPoint = startPoint;
	}
	public Double getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(Double endPoint) {
		this.endPoint = endPoint;
	}
	public Double getQuickDeduction() {
		return quickDeduction;
	}
	public void setQuickDeduction(Double quickDeduction) {
		this.quickDeduction = quickDeduction;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	
}
