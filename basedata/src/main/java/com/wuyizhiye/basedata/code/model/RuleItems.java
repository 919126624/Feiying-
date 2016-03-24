package com.wuyizhiye.basedata.code.model;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.code.enums.PropertyEnum;

/**
 * @ClassName RuleItems
 * @Description 自定义编码规则明细实体
 * @author li.biao
 * @date 2015-4-2
 */
public class RuleItems extends DataEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PropertyEnum property; //系统属性
	private String format; // 针对实际的格式化
	private int numLength; //长度
	private Rules rules; //编码规则
	private int orderBy; //排序字段
	
	public PropertyEnum getProperty() {
		return property;
	}
	public void setProperty(PropertyEnum property) {
		this.property = property;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public int getNumLength() {
		return numLength;
	}
	public void setNumLength(int numLength) {
		this.numLength = numLength;
	}
	public Rules getRules() {
		return rules;
	}
	public void setRules(Rules rules) {
		this.rules = rules;
	}
	public int getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}
	
	
}
