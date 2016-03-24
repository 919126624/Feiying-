package com.wuyizhiye.cmct.phone.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DialResultEnum
 * @Description 呼叫中心  通话结果描述
 * @author li.biao
 * @date 2015-5-26
 */
public enum DialResultEnum {

	C_121("发起拨打","C_121"),
	C_122("服务器响应","C_122"),
	C_124("通话中","C_124"),
	C_125("挂机","C_125"),
	C_127("呼叫失败","C_127"),
	C_128("呼叫成功","C_128"),
	;
	
	private String name;
	private String value;
	private DialResultEnum(String name,String value){
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
		DialResultEnum[] ary = DialResultEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	public static DialResultEnum getEnumByValue(String value) {
		DialResultEnum[] enums = DialResultEnum.values();
		for (DialResultEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
