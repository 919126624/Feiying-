package com.wuyizhiye.cmct.wechat.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WechatCommStatusEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public enum WechatCommStatusEnum {

	PICT("图文","PICT"),
	TEXT("纯文本","TEXT");
	
	private String name;
	private String value;
	private WechatCommStatusEnum(String name,String value){
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
		WechatCommStatusEnum[] ary = WechatCommStatusEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static WechatCommStatusEnum getEnumByValue(String value) {
		WechatCommStatusEnum[] enums = WechatCommStatusEnum.values();
		for (WechatCommStatusEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
