package com.wuyizhiye.base.common.enums;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DataTypeEnum
 * @Description 流程参数数据类型
 * @author li.biao
 * @date 2015-4-1
 */
public enum DataTypeEnum {
	
	Date("日期","Date"),
	Double("浮点型对象","Double"),
	double_("浮点型","double_"),
	Integer("整数对象","Integer"),
	int_("整数","int_"),
	Long("长整型对象","Long"),
	long_("长整型","long_"),
	Short("小数对象","Short"),
	short_("小数","short_"),
	String("字符","String")
	;
	
	private String label;
	private String value;
	private DataTypeEnum(String label,String value){
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
		DataTypeEnum[] ary = DataTypeEnum.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].getLabel());
			list.add(map);
		}
		return list;
	}
	
	public static DataTypeEnum getEnumByValue(String value) {
		DataTypeEnum[] enums = DataTypeEnum.values();
		for (DataTypeEnum en : enums) {
			if (en.getValue().equals(value))
				return en;
		}

		return null;
	}
	
	public static Class getClassByEnum(DataTypeEnum type){
		 if(type!=null){
				String t=type.toString();
				switch (type) {  
				case Date :
					return  Date.class;
				case Double :
					return  Double.class;
				case double_ :
					return  double.class;
				case Integer :
					return  Integer.class;
				case int_:
					return  int.class;
				case Long:
					return  Long.class;
				case long_:
					return  long.class;
				case Short:
					return  Short.class;
				case short_:
					return  short.class;
				case String:
					return  String.class;
				default:
					return Object.class;
				}
			} 
			return Object.class;
	 }
}
