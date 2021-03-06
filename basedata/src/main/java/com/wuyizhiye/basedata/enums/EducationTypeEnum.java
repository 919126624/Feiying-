package com.wuyizhiye.basedata.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EducationTypeEnum
 * @Description 学历
 * @author li.biao
 * @date 2015-4-2
 */
public enum EducationTypeEnum {
	PRIMARYSCHOOL("小学","PRIMARYSCHOOL"),
	MIDDLESCHOOL("初中","MIDDLESCHOOL"),
	SENIORSCHOOL("高中","SENIORSCHOOL"),
	SPECIALSCHOOL("中专","SPECIALSCHOOL"),
	JUNIORCOLLEGE("大专","JUNIORCOLLEGE"),
	UNIVERSITY("本科","UNIVERSITY"),
	POSTGRADUATE("研究生","POSTGRADUATE"),
	MASTER("硕士","MASTER"),
	DOCTOR("博士","DOCTOR");
	private String label;
	private String value;
	private EducationTypeEnum(String label,String value){
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
		EducationTypeEnum[] ary = EducationTypeEnum.values();
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
