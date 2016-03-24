package com.wuyizhiye.basedata.person.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RecruitMethodEnum
 * @Description 招聘渠道
 * @author li.biao
 * @date 2015-4-3
 */
public enum RecruitMethodEnum {
	NET("网络招聘",""),
	MARKET("人才市场",""),
	SCHOOL("校园招聘",""),
	COMPANY("内部推荐",""),
	BACK("离职回岗",""),
	OTHER("其他","");
	
	private String label;
	private String value;
	private RecruitMethodEnum(String label,String value){
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
		RecruitMethodEnum[] ary = RecruitMethodEnum.values();
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
