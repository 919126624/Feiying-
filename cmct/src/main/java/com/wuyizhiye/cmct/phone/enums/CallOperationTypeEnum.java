package com.wuyizhiye.cmct.phone.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CallOperationTypeEnum
 * @Description 话务明细业务类型枚举
 * @author li.biao
 * @date 2015-5-26
 */
public enum CallOperationTypeEnum {
	LOCALPHONE("本地手机","LOCALPHONE");
	private String name;
	private String value;
	private CallOperationTypeEnum(String name,String value){
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
		CallOperationTypeEnum[] ary = CallOperationTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static CallOperationTypeEnum getEnumByValue(String value) {
		CallOperationTypeEnum[] enums = CallOperationTypeEnum.values();
		for (CallOperationTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
