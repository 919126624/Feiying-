package com.wuyizhiye.hr.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审批枚举
 * @author Cai.xing
 * @since 2013-04-02
 * */
public enum BillStatusEnum {
	SAVE("未提交","SAVE"),
	SUBMIT("审批中","SUBMIT"),
	APPROVED("已审批","APPROVED"),
	REVOKE("已撤销","REVOKE"),
	REJECT("已驳回","REJECT")
	;
	private String label;
	private String value;
	private BillStatusEnum(String label,String value){
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
		BillStatusEnum[] ary = BillStatusEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getLabel());
			list.add(map);
		}
		return list;
	}
	
	public static BillStatusEnum getEnumByValue(String value) {
		BillStatusEnum[] enums = BillStatusEnum.values();
		for (BillStatusEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
