package com.wuyizhiye.basedata.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LoginPageModelEnum
 * @Description 登陆页面风格模板
 * @author li.biao
 * @date 2015-4-2
 */
public enum LoginPageModelEnum {
	MODLE1("蓝色风","MODLE1"),
	MODLE2("新的现代风 ","MODLE2");
	private String label;
	private String value;
	private LoginPageModelEnum(String label,String value){
		this.label = label;
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(){
		LoginPageModelEnum[] ary = LoginPageModelEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getLabel());
			list.add(map);
		}
		return list;
	}
}
