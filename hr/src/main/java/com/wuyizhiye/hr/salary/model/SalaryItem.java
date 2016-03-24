package com.wuyizhiye.hr.salary.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.hr.enums.SalaryItemTypeEnum;
import com.wuyizhiye.hr.enums.StatusEnum;

/**
 * 薪酬项目
 * @author hyl
 * @since 2014-02-12
 *
 */
public class SalaryItem extends CoreEntity{
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
	 * 编号
	 */
	private String number;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 状态：是否启用
	 */
	private StatusEnum status;
	/**
	 * 描述
	 */
	private String remark ;
	/**
	 * 方式
	 */
	private SalaryItemTypeEnum itemType;
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public StatusEnum getStatus() {
		return status;
	}
	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public SalaryItemTypeEnum getItemType() {
		return itemType;
	}
	public void setItemType(SalaryItemTypeEnum itemType) {
		this.itemType = itemType;
	}
	
}
