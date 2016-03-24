package com.wuyizhiye.hr.enums;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销假状态
 * @author hyl
 * @since 2013-11-18
 */
public enum LeaveClearanceStatusEnum {
	NOLEAVECLEARANCE("未销假","NOLEAVECLEARANCE"),
	LEAVECLEARANCE("已销假","LEAVECLEARANCE"),
	SUBMITTED("已提交","SUBMITTED");
	private String label;
	private String value;
	private LeaveClearanceStatusEnum(String label,String value){
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
		LeaveClearanceStatusEnum[] ary = LeaveClearanceStatusEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getLabel());
			list.add(map);
		}
		return list;
	}
	
	public static LeaveClearanceStatusEnum getEnumByValue(String value) {
		LeaveClearanceStatusEnum[] enums = LeaveClearanceStatusEnum.values();
		for (LeaveClearanceStatusEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
}
