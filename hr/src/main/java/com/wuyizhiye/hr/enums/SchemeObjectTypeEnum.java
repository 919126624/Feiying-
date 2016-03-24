package com.wuyizhiye.hr.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 薪酬方案实施对象类型
 * @author hlz
 *@since 2014-02-13
 */
public enum SchemeObjectTypeEnum {
 
	COMPANY("全公司","COMPANY"),
	ORG("部门","ORG"),
	JOB("职务","JOB");
	
	private String name;
	
	private String value;

	private SchemeObjectTypeEnum(String name,String value){
		this.name = name;
		this.value = value;
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
	 
	
	public static List toList(){
		SchemeObjectTypeEnum[] ary = SchemeObjectTypeEnum.values();
		List status = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			status.add(map);
		}
		return status;
	}
}
