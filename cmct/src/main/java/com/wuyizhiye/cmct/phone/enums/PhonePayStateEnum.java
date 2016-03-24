package com.wuyizhiye.cmct.phone.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PhonePayStateEnum
 * @Description  充值状态：正在申请中，已经充值
 * NO--删除
 * @author li.biao
 * @date 2015-5-26
 */
public enum PhonePayStateEnum {
	APPLY("正在申请", "APPLY"), 
	FINISH("已经充值", "FINISH");
	
	private String name;
	private String value;

	private PhonePayStateEnum(String name, String value) {
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
		PhonePayStateEnum[] ary = PhonePayStateEnum.values();
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
