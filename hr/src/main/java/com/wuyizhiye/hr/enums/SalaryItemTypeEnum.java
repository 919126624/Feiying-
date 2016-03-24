package com.wuyizhiye.hr.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 薪酬项目_方式
 * @author hyl
 * @since 2014-02-12 
 */
public enum SalaryItemTypeEnum {
	MANUAL("手动输入","MANUAL"),
	FORMULA("公式获取","FORMULA");
	private String label;
	private String value;
	private SalaryItemTypeEnum(String label,String value){
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
		SalaryItemTypeEnum[] ary = SalaryItemTypeEnum.values();
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
