package com.wuyizhiye.cmct.phone.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PhoneMemberUseEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public enum PhoneMemberUseEnum {
	TEL("固定电话","TEL"),
	RADIO("无线电话","RADIO"),
	PHONE("手机","PHONE");
	
	private String name;
	private String value;
	private PhoneMemberUseEnum(String name,String value){
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(){
		DialResultEnum[] ary = DialResultEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static DialResultEnum getEnumByValue(String value) {
		DialResultEnum[] enums = DialResultEnum.values();
		for (DialResultEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
