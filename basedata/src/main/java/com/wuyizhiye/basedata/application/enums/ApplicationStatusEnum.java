package com.wuyizhiye.basedata.application.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ApplicationStatusEnum
 * @Description  mac申请审批枚举
 * @author li.biao
 * @date 2015-4-2
 */
public enum ApplicationStatusEnum {
	SAVE("已提交","SAVE"),
	APPROVED("已审批","APPROVED"),
	REJECT("已驳回","REJECT")
	;
	private String label;
	private String value;
	private ApplicationStatusEnum(String label,String value){
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
		ApplicationStatusEnum[] ary = ApplicationStatusEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("label", ary[i].getLabel());
			list.add(map);
		}
		return list;
	}
	
	public static ApplicationStatusEnum getEnumByValue(String value) {
		ApplicationStatusEnum[] enums = ApplicationStatusEnum.values();
		for (ApplicationStatusEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
