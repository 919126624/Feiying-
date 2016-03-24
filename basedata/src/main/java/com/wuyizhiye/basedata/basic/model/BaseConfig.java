package com.wuyizhiye.basedata.basic.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName BaseConfig
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class BaseConfig extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5543274218182293612L;

	
	private String number;
	
	private String name;
	
	private String paramValue;

	public BaseConfig(){
		
	}
	
	public BaseConfig(String number,String name,String paramValue){
		this.name = name;
		this.number = number;
		this.paramValue = paramValue;
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

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
	
}
