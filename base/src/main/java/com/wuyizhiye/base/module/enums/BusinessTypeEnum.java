package com.wuyizhiye.base.module.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行业枚举
 * @ClassName BusinessTypeEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-4-1
 */
public enum BusinessTypeEnum {
	
	TYHY(null,"通用行业","TYHY"),
	FDCZJ(null,"房地产中介","FDCZJ"),
	//FDCKF(null,"房地产开发","FDCKF"),
	//DCWY(null,"房地产物业","DCWY"),
	//DONGQ(null,"拆迁征收","CQZS"),
	JRDB(null,"金融担保","JRDB"),
	JRZJ(null,"金融中介","JRZJ"),
	RJHY(null,"软件行业","RJHY"),
	//BXZJ(null,"保险中介","BXZJ")
	;
	private String label;
	private String value;
	private String name;
	private BusinessTypeEnum parent;
	
	private BusinessTypeEnum(BusinessTypeEnum parent,String label,String value){
		this.parent = parent ;
		this.label = label;
		this.name = label ;
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
	public String getName() {
		return name;
	}
	public BusinessTypeEnum getParent() {
		return parent;
	}
	
	/**
	 * 转LIST
	 * @return
	 */
	public static List<Map<String, String>> toList(){
		BusinessTypeEnum[] ary = BusinessTypeEnum.values();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getLabel());
			list.add(map);
		}
		return list;
	}
}
