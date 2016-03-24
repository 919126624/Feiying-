package com.wuyizhiye.cmct.sms.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SMSOperationTypeEnum
 * @Description 短信控制  控制类型
 * @author li.biao
 * @date 2015-5-26
 */
public enum SMSOperationTypeEnum {
	OPEN_SET_TYPE("开通","OPEN_SET_TYPE"),
	CLOSE_SET_TYPE("关闭","CLOSE_SET_TYPE"),
	MODIFY_SET_TYPE("改设置","MODIFY_SET_TYPE"),
	CHARGE_TYPE("充值","CHARGE_TYPE"),
	DEDUCTION_TYPE("扣减","DEDUCTION_TYPE"),
	GIVE_MESSAGE("短信赠送","GIVE_MESSAGE"),
	RECEIVE_MESSAGE("短信收赠","RECEIVE_MESSAGE")
	;
	private String name;
	private String value;
	private SMSOperationTypeEnum(String name,String value){
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
		SMSOperationTypeEnum[] ary = SMSOperationTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static SMSOperationTypeEnum getEnumByValue(String value) {
		SMSOperationTypeEnum[] enums = SMSOperationTypeEnum.values();
		for (SMSOperationTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
