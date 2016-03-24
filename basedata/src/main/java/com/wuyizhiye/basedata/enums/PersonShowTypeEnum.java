package com.wuyizhiye.basedata.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PersonShowTypeEnum
 * @Description 人员信息显示控制类型
 * @author li.biao
 * @date 2015-4-2
 */
public enum PersonShowTypeEnum {
	ALL("显示所有","ALL"),
	PHONE("手机号","PHONE"),
	SHORTNUMBER("短号","SHORTNUMBER"),
	NO_SHOW("不显示","NO_SHOW");
	private String label;
	private String value;
	private PersonShowTypeEnum(String label,String value){
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
		PersonShowTypeEnum[] ary = PersonShowTypeEnum.values();
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
