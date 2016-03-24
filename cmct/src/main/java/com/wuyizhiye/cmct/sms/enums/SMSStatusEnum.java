package com.wuyizhiye.cmct.sms.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SMSStatusEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public enum SMSStatusEnum {
	SEND_ING("发送中","SEND_ING"),
	SEND_SUCCESS("发送成功","SEND_SUCCESS"),
	SEND_FAIL("发送失败","SEND_FAIL")
	;
	private String name;
	private String value;
	private SMSStatusEnum(String name,String value){
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(){
		SMSTypeEnum[] ary = SMSTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static SMSTypeEnum getEnumByValue(String value) {
		SMSTypeEnum[] enums = SMSTypeEnum.values();
		for (SMSTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
