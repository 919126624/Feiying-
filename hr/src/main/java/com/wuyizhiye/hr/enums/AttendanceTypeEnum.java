package com.wuyizhiye.hr.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考勤类型
 * @author ouyangyi
 * @since 2013-11-19 上午11:39:37
 */
public enum AttendanceTypeEnum {
	ACTUALDAY("出勤","actualDay"),
	COMPASSIONATELEAVE("事假","compassionateLeave"),
	SICKLEAVE("病假","sickLeave"),
	RESTDAYS("休息","restDays"),
	ANNUALLEAVE("年假","annualLeave"),
	MARRIAGELEAVE("婚假","marriageLeave"),
	BEREAVEMENTLEAVE("丧假","bereavementLeave"),
	IPPFLEAVE("计生假","IPPFLeave"),
	SKIPWORKLEAVE("旷工","skipWorkLeave"),
	NOENTRY("未入职","noEntry"),
	OTHERHOLIDAYS("其它假","otherHolidays")
	;
	 
	private String name;
	private String value;
	private AttendanceTypeEnum(String name,String value){
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
		AttendanceTypeEnum[] ary = AttendanceTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].getValue());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static AttendanceTypeEnum getEnumByValue(String value) {
		AttendanceTypeEnum[] enums = AttendanceTypeEnum.values();
		for (AttendanceTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
