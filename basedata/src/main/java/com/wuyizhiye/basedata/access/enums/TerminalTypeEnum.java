package com.wuyizhiye.basedata.access.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TerminalTypeEnum
 * @Description 终端类型类型
 * @author li.biao
 * @date 2015-4-2
 */
public enum TerminalTypeEnum {

	COMPUTER("台式电脑","COMPUTER"),
	LAPTOP("笔记本","LAPTOP"),
	MOBILEPHONE("手机","MOBILEPHONE"),
	TABLETPC("平板","TABLETPC"),
	;
	
	private String name;
	private String value;
	private TerminalTypeEnum(String name,String value){
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
		TerminalTypeEnum[] ary = TerminalTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static TerminalTypeEnum getEnumByValue(String value) {
		TerminalTypeEnum[] enums = TerminalTypeEnum.values();
		for (TerminalTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
