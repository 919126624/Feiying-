package com.wuyizhiye.hr.enums;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请假类型
 * @author hyl
 * @since 2013-11-18
 */
public enum Ask4LeaveTypeEnum {
	COMPASSIONATELEAVE("事假","COMPASSIONATELEAVE"),
	SICKLEAVE("病假","SICKLEAVE"),
	ANNUALLEAVE("年假","ANNUALLEAVE"),
	MARRIAGELEAVE("婚假","MARRIAGELEAVE"),
	BEREAVEMENTLEAVE("丧假","BEREAVEMENTLEAVE"),
	IPPFLEAVE("计生假","IPPFLEAVE"),
	OHTER("其他","OHTER");
	private String label;
	private String value;
	private Ask4LeaveTypeEnum(String label,String value){
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
		Ask4LeaveTypeEnum[] ary = Ask4LeaveTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getLabel());
			list.add(map);
		}
		return list;
	}
	
	public static Ask4LeaveTypeEnum getEnumByValue(String value) {
		Ask4LeaveTypeEnum[] enums = Ask4LeaveTypeEnum.values();
		for (Ask4LeaveTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
