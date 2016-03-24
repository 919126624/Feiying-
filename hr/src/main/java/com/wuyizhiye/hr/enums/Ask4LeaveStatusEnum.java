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
public enum Ask4LeaveStatusEnum {
	SAVE("未提交","SAVE"),
	//SUBMITTED("已提交","SUBMITTED"),
	SUBMIT("审批中","SUBMIT"),
	REJECT("已驳回","REJECT"),
	REVOKE("已撤销","REVOKE"),
	APPROVED("已审批","APPROVED")
	;
	private String label;
	private String value;
	private Ask4LeaveStatusEnum(String label,String value){
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
		Ask4LeaveStatusEnum[] ary = Ask4LeaveStatusEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("label", ary[i].getLabel());
			list.add(map);
		}
		return list;
	}
	
	public static Ask4LeaveStatusEnum getEnumByValue(String value) {
		Ask4LeaveStatusEnum[] enums = Ask4LeaveStatusEnum.values();
		for (Ask4LeaveStatusEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
