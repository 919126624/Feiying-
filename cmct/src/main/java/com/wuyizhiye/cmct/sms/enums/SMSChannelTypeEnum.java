package com.wuyizhiye.cmct.sms.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SMSChannelTypeEnum
 * @Description 发送类别
 * @author li.biao
 * @date 2015-5-26
 */
public enum SMSChannelTypeEnum {
	STANDARD_CHANNEL("标准通道","STANDARD_CHANNEL"),
	BATCH_CHANNEL("批送通道","BATCH_CHANNEL")
	;
	private String name;
	private String value;
	private SMSChannelTypeEnum(String name,String value){
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
		SMSChannelTypeEnum[] ary = SMSChannelTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static SMSChannelTypeEnum getEnumByValue(String value) {
		SMSChannelTypeEnum[] enums = SMSChannelTypeEnum.values();
		for (SMSChannelTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
