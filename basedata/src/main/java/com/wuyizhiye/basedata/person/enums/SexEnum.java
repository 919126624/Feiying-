package com.wuyizhiye.basedata.person.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SexEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public enum SexEnum {
	MAN("男","MAN"),
	WOMAN("女","WOMAN");
	private String name;
	private String value;
	private SexEnum(String name,String value){
		this.setName(name);
		this.setValue(value);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(){
		SexEnum[] ary = SexEnum.values();
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
