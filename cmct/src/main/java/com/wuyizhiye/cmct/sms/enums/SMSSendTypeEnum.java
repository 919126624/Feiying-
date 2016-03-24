package com.wuyizhiye.cmct.sms.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SMSSendTypeEnum
 * @Description 发送类别
 * @author li.biao
 * @date 2015-5-26
 */
public enum SMSSendTypeEnum {
	STANDARD_SMS("标准短信","STANDARD_SMS"),
	TIMING_SMS("定时短信","TIMING_SMS"),
	BATCH_SMS("批送短信","BATCH_SMS")
	;
	private String name;
	private String value;
	private SMSSendTypeEnum(String name,String value){
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
		SMSSendTypeEnum[] ary = SMSSendTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static SMSSendTypeEnum getEnumByValue(String value) {
		SMSSendTypeEnum[] enums = SMSSendTypeEnum.values();
		for (SMSSendTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
