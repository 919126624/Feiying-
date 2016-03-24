package com.wuyizhiye.cmct.sms.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SMSControlTypeEnum
 * @Description 短信控制  控制类型
 * @author li.biao
 * @date 2015-5-26
 */
public enum SMSControlTypeEnum {
	PERSONAL_TYPE("个人","PERSONAL_TYPE"),
	DEPT_TYPE("部门","DEPT_TYPE"),
	COMPANY_TYPE("公司","COMPANY_TYPE")
	;
	private String name;
	private String value;
	private SMSControlTypeEnum(String name,String value){
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
		SMSControlTypeEnum[] ary = SMSControlTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static SMSControlTypeEnum getEnumByValue(String value) {
		SMSControlTypeEnum[] enums = SMSControlTypeEnum.values();
		for (SMSControlTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
