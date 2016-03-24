package com.wuyizhiye.basedata.person.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JobStatusEnum
 * @Description 岗位状态
 * @author li.biao
 * @date 2015-4-3
 */
public enum JobStatusEnum {
	ONDUTY("在职","ONDUTY"),
	STOPSALARYONDUTY("停薪留职","STOPSALARYONDUTY"),
	STAYUNTILRETIRE("留退休","STAYUNTILRETIRE"),
	DIMISSION("离职","DIMISSION"),
	PROBATION("见习","PROBATION"),
	RUNDISK("跑盘员","RUNDISK")
	; 
	private String label;
	private String value;
	private JobStatusEnum(String label,String value){
		this.setLabel(label);
		this.setValue(value);
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
		JobStatusEnum[] ary = JobStatusEnum.values();
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
