package com.wuyizhiye.hr.salary.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.hr.enums.SchemeObjectTypeEnum;

/**
 * 薪酬方案
 * @author hlz
 * @since 2014-02-13
 *
 */
public class SalaryScheme extends CoreEntity {
	private static final long serialVersionUID = -5002130462652639382L;
	/**
	 * 方案名称
	 */
	private String name;
	/**
	 * 方案实施对象类型
	 */
	private SchemeObjectTypeEnum objectType;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 创建人
	 */
	private Person creator;
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 状态
	 */
	private int state;
	//*************==========以下属性只是为了方便查询===========*******************//
	/**
	 * 方案实施对象
	 */
	private List<SalarySchemeObject> salarySchemeObjectList = new ArrayList<SalarySchemeObject>();
	/**
	 * 方案项目
	 */
	private List<SalarySchemeItem> salarySchemeItem = new ArrayList<SalarySchemeItem>();
	
	private String orgName;
	
	private String positionName;
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SchemeObjectTypeEnum getObjectType() {
		return objectType;
	}
	public void setObjectType(SchemeObjectTypeEnum objectType) {
		this.objectType = objectType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Person getCreator() {
		return creator;
	}
	public void setCreator(Person creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public List<SalarySchemeObject> getSalarySchemeObjectList() {
		return salarySchemeObjectList;
	}
	public void setSalarySchemeObjectList(
			List<SalarySchemeObject> salarySchemeObjectList) {
		this.salarySchemeObjectList = salarySchemeObjectList;
	}
	public List<SalarySchemeItem> getSalarySchemeItem() {
		return salarySchemeItem;
	}
	public void setSalarySchemeItem(List<SalarySchemeItem> salarySchemeItem) {
		this.salarySchemeItem = salarySchemeItem;
	}
	
	
}
