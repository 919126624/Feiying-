package com.wuyizhiye.basedata.person.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName OperateTypeEnum
 * @Description 任职历史操作日志 
 * @author li.biao
 * @date 2015-4-3
 */
public enum OperateTypeEnum {
	ADD("新增","ADD"),
	EDIT("修改","EDIT"),
	DELETE("删除","DELETE");
	
	private String name;
	private String value;
	private OperateTypeEnum(String name,String value){
		this.setName(name);
		this.setValue(value);
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
		OperateTypeEnum[] ary = OperateTypeEnum.values();
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
