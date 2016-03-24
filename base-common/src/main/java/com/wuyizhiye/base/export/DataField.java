package com.wuyizhiye.base.export;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName DataField
 * @Description 描述: 数据Field
 * @author li.biao
 * @date 2015-4-1
 */
public class DataField extends CoreEntity{
	
	private static final long serialVersionUID = 6485814331192914480L;
	
	/**
	 * 字段名称
	 */
	private String name;
	
	/**
	 * 字段名
	 */
	private String field;
	
	/**
	 * <p>Title: DataField.java</p>  
	 * <p>Description: 构造函数</p>
	 */
	public DataField(){
	
	}
	
	public DataField(String name,String field){
		this.name = name;
		this.field = field;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
}
