package com.wuyizhiye.cmct.sms.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SMSStrategyTypeEnum
 * @Description 短信控制  上限策略类型
 * @author li.biao
 * @date 2015-5-26
 */
public enum SMSStrategyTypeEnum {
	BY_MONTH_TYPE("按月","BY_MONTH_TYPE"),
	BY_DAY_TYPE("按日","BY_DAY_TYPE")
	;
	private String name;
	private String value;
	private SMSStrategyTypeEnum(String name,String value){
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
		SMSStrategyTypeEnum[] ary = SMSStrategyTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static SMSStrategyTypeEnum getEnumByValue(String value) {
		SMSStrategyTypeEnum[] enums = SMSStrategyTypeEnum.values();
		for (SMSStrategyTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
