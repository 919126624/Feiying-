package com.wuyizhiye.hr.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人员(组织、岗位、职级)异动类型枚举
 * @author ouyangyi
 * @since 2013-04-07
 * */
public enum PositionChangeTypeEnum {
	RUNDISK("新增跑盘","RUNDISK"),
	DELRUNDISK("删除跑盘","DELRUNDISK"),
	ENROLL("入职","ENROLL"),
	TRANSFER("调职","TRANSFER"),
	PROMOTION("晋升","PROMOTION"),
	DEMOTION("降职","DEMOTION"),
	INCREASE_PARTTIMEJOB("兼职","INCREASE_PARTTIMEJOB"),
	DISMISS_PARTTIMEJOB("撤职","DISMISS_PARTTIMEJOB"),
	LEAVE("离职","LEAVE"),
	POSITIVE("转正","POSITIVE"),
	REINSTATEMENT("复职","REINSTATEMENT")
	;
	private String label;
	private String value;
	private PositionChangeTypeEnum(String label,String value){
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
		PositionChangeTypeEnum[] ary = PositionChangeTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getLabel());
			list.add(map);
		}
		return list;
	}
	
	public static PositionChangeTypeEnum getEnumByValue(String value) {
		PositionChangeTypeEnum[] enums = PositionChangeTypeEnum.values();
		for (PositionChangeTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}

}
