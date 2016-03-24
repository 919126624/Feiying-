package com.wuyizhiye.basedata.sql.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SqlRunStatusEnum
 * @Description sql升级执行状态
 * @author li.biao
 * @date 2015-4-3
 */
public enum SqlRunStatusEnum {
	/**
	 * 待执行
	 */
	WAIT("WAIT","待执行"),
	
	/**
	 * 己执行
	 */
	RUNED("RUNED","执行成功"),
	
	/**
	 * 执行失败
	 */
	FAIL("FAIL","执行失败"),
	
	/**
	 * 己取消
	 */
	CANCLE("CANCLE","己取消");
	
	private String name;
	private String label;
	private SqlRunStatusEnum(String name,String label){
		this.label = label;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * 转LIST
	 * @return
	 */
	public static List<Map<String, String>> toList(){
		SqlRunStatusEnum[] ary = SqlRunStatusEnum.values();
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
