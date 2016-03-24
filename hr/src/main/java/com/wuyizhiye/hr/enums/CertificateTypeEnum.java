package com.wuyizhiye.hr.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 证件类型
 * @author taking.wang
 * 
 * @since 2012-13-01-15
 */
public enum CertificateTypeEnum {
	NATION("全国","NATION"),
	NATIVE("本地","NATIVE");
	private String label;
	private String value;
	private CertificateTypeEnum(String label,String value){
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
		CertificateTypeEnum[] ary = CertificateTypeEnum.values();
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
