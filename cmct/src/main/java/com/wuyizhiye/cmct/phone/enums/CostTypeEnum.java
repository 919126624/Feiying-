package com.wuyizhiye.cmct.phone.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DialResultEnum
 * @Description 
 * @author li.biao
 * @date 2015-5-26
 */
public enum CostTypeEnum {

	TAKECOST("充值", "TAKECOST"), 
	GIVING("赠送", "GIVING"), 
	DEDUCT("扣除", "DEDUCT");
	
	private String name;
	private String value;
	private CostTypeEnum(String name,String value){
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
		CostTypeEnum[] ary = CostTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static CostTypeEnum getEnumByValue(String value) {
		CostTypeEnum[] enums = CostTypeEnum.values();
		for (CostTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
