package com.wuyizhiye.basedata;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName DataEntity
 * @Description 数据实体基类
 * @author li.biao
 * @date 2015-4-2
 */
public abstract class DataEntity extends CoreEntity {
	private static final long serialVersionUID = 2764148395385508786L;
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 编码
	 */
	private String number;
	
	/**
	 * 拼音
	 */
	private String simplePinyin;
	
	/**
	 * 描述
	 */
	private String description;
	
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
	 * 数据所属CU
	 */
	private Org controlUnit;
	
	/**
	 * 数据所属组织
	 */
	private Org org;
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
		
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTimeStr() {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(null != this.createTime){
			return df.format(this.createTime);
		}
		return "";
	}

	/**
	 * @param lastUpdateTime the lastUpdateTime to set
	 */
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * @return the lastUpdateTime
	 */
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	
	

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(Person creator) {
		this.creator = creator;
	}

	/**
	 * @return the creator
	 */
	public Person getCreator() {
		return creator;
	}

	public void setSimplePinyin(String simplePinyin) {
		this.simplePinyin = simplePinyin;
	}

	public String getSimplePinyin() {
		return simplePinyin;
	}

	public void setUpdator(Person updator) {
		this.updator = updator;
	}

	public Person getUpdator() {
		return updator;
	}

	public void setControlUnit(Org controlUnit) {
		this.controlUnit = controlUnit;
	}

	public Org getControlUnit() {
		return controlUnit;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Org getOrg() {
		return org;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
