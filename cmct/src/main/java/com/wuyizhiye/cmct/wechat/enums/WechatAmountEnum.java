package com.wuyizhiye.cmct.wechat.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WechatAmountEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public enum WechatAmountEnum {

	READ("阅读","READ"),
	REGIST("注册","REGIST");
	
	private String name;
	private String value;
	private WechatAmountEnum(String name,String value){
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
		WechatAmountEnum[] ary = WechatAmountEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static WechatAmountEnum getEnumByValue(String value) {
		WechatAmountEnum[] enums = WechatAmountEnum.values();
		for (WechatAmountEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
