package com.wuyizhiye.hr.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考勤审批枚举
 * @author ouyangyi
 * @since 2013-5-22 下午05:41:27
 */
public enum AttendanceStatusEnum {
	SAVE("未提交","SAVE"),
	SUBMIT("审批中","SUBMIT"),
	REJECT("已驳回","REJECT"),
	APPROVED("已审批","APPROVED")
	;
	private String label;
	private String value;
	private AttendanceStatusEnum(String label,String value){
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
		AttendanceStatusEnum[] ary = AttendanceStatusEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("label", ary[i].getLabel());
			list.add(map);
		}
		return list;
	}
	
	public static AttendanceStatusEnum getEnumByValue(String value) {
		AttendanceStatusEnum[] enums = AttendanceStatusEnum.values();
		for (AttendanceStatusEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
