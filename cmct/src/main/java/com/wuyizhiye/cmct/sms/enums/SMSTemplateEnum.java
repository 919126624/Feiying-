package com.wuyizhiye.cmct.sms.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SMSTemplateEnum
 * @Description 启用状态
 * @author li.biao
 * @date 2015-5-26
 */
public enum SMSTemplateEnum {
	ENABLED("启用","ENABLED"),
	DISABLE("禁用","DISABLE");
	
	private String name;
	private String value;
	private SMSTemplateEnum(String name,String value){
		this.setName(name);
		this.setValue(value);
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(){
		SMSTemplateEnum[] ary = SMSTemplateEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
}
