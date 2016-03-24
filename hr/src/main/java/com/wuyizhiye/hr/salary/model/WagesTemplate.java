package com.wuyizhiye.hr.salary.model;
import java.util.Date;
import java.util.List;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;
/**
 * 工资模板
 * @author hly
 *
 */
public class WagesTemplate extends CoreEntity{
	
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
	private String wageNumber;
	// 缺省模板
	private CommonFlagEnum isDefult ;
	/**
	 *  名称
	 */
	private String name;
	/**
	 *  描述
	 */
	private String remark;
	/**
	 * 岗位
	 */
	private Job job;
	
	/**
	 * 组织
	 */
	private Org org;
	/**
	 * 非表中字段
	 * @return
	 */
	private List<WagesItem> wagesItemList;//薪酬项目
	private String detailJson;//表单提交时用

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

	public String getWageNumber() {
		return wageNumber;
	}

	public void setWageNumber(String wageNumber) {
		this.wageNumber = wageNumber;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public CommonFlagEnum getIsDefult() {
		return isDefult;
	}

	public void setIsDefult(CommonFlagEnum isDefult) {
		this.isDefult = isDefult;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<WagesItem> getWagesItemList() {
		return wagesItemList;
	}

	public void setWagesItemList(List<WagesItem> wagesItemList) {
		this.wagesItemList = wagesItemList;
	}

	public String getDetailJson() {
		return detailJson;
	}

	public void setDetailJson(String detailJson) {
		this.detailJson = detailJson;
	}
}
