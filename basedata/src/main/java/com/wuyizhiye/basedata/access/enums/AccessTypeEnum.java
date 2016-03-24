package com.wuyizhiye.basedata.access.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AccessTypeEnum
 * @Description 终端许可权限类型
 * @author li.biao
 * @date 2015-4-2
 */
public enum AccessTypeEnum {

	SHARE("全员共享","SHARE"),
	ORGSHARE("部门共享","ORGSHARE"),
	ORGTREESHARE("部门及下级部门共享","ORGTREESHARE"),
	PRIVATE("个人专用","PRIVATE")
	;
	
	private String name;
	private String value;
	private AccessTypeEnum(String name,String value){
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
		AccessTypeEnum[] ary = AccessTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static AccessTypeEnum getEnumByValue(String value) {
		AccessTypeEnum[] enums = AccessTypeEnum.values();
		for (AccessTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
