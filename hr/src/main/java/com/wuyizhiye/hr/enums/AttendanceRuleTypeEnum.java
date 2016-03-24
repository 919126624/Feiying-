package com.wuyizhiye.hr.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规则类型（五天制、六天制、七天制)
 * @author ouyangyi
 * @since 2013-11-19 上午11:39:37
 */
public enum AttendanceRuleTypeEnum {
	FIVE("五天制","5"),
	SIX("六天制","6"),
	SEVEN("七天制","7")
	;
	private String name;
	private String value;
	private AttendanceRuleTypeEnum(String name,String value){
		this.name = name;
		this.value = value;
	}
	 
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(){
		AttendanceRuleTypeEnum[] ary = AttendanceRuleTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static AttendanceRuleTypeEnum getEnumByValue(String value) {
		AttendanceRuleTypeEnum[] enums = AttendanceRuleTypeEnum.values();
		for (AttendanceRuleTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
