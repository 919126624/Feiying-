package com.wuyizhiye.cmct.ucs.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.cmct.phone.enums.StateEnum;

/**
 * @ClassName UcsPhoneDealerEnum
 * @Description 坐席的类别
 * @author li.biao
 * @date 2015-5-26
 */
public enum UcsPhoneDealerEnum {
	ZERO("管理员", "ZERO"), 
	ONE("代理", "ONE"), 
	TWO("机构", "TWO"),
	THREE("散户", "THREE"), 
	FORE("坐席", "FORE"), 
	FIVE("班长", "FIVE"),
	SIX("财务角色", "SIX"), 
	SEVVEN("维护员角色", "SEVVEN"), 
	EIGHT("审核员角色", "EIGHT");

	private String name;
	private String value;

	private UcsPhoneDealerEnum(String name, String value) {
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
		StateEnum[] ary = StateEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}

}

