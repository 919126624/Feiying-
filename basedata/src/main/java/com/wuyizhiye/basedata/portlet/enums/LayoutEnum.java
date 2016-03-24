package com.wuyizhiye.basedata.portlet.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LayoutEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public enum LayoutEnum {
	ONECOL("平分模式","ONECOL"),
	TWOCOL("七三模式","TWOCOL"),
	THREECOL("其他模式","THREECOL");
	private String title;
	private String value;
	private LayoutEnum(String title,String value){
		this.title = title;
		this.value = value;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 转LIST
	 * @return
	 */
	public static List<Map<String, String>> toList(){
		LayoutEnum[] ary = LayoutEnum.values();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getTitle());
			list.add(map);
		}
		return list;
	}
	
}
