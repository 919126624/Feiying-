package com.wuyizhiye.basedata.person.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CardTypeEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public enum CardTypeEnum {

	IDCARD("身份证","IDCARD"),
	PASSPORT("护照","PASSPORT"),
	HK_MAC("港澳身份证","HK_MAC"),
	RESTSCARD("其他","RESTSCARD");

	private String name;
	
	private String value;

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

	private CardTypeEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(){
		JobStatusEnum[] ary = JobStatusEnum.values();
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
