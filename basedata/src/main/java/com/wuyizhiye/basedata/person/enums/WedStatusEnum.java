package com.wuyizhiye.basedata.person.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WedStatusEnum
 * @Description 婚姻状况
 * @author li.biao
 * @date 2015-4-3
 */
public enum WedStatusEnum {
	NOTMARRIED("未婚","NOTMARRIED"),
	MARRIED("已婚","MARRIED"),
	DIVORCE("离婚","DIVORCE");
	
	private String label;
	private String value;
	private WedStatusEnum(String label,String value){
		this.setLabel(label);
		this.setValue(value);
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(){
		WedStatusEnum[] ary = WedStatusEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getLabel());
			list.add(map);
		}
		return list;
	}
	
}
