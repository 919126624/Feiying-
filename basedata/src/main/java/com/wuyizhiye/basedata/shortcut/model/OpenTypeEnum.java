package com.wuyizhiye.basedata.shortcut.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.basedata.enums.LoginPageModelEnum;

/**
 * @ClassName OpenTypeEnum
 * @Description 打开类型
 * @author li.biao
 * @date 2015-4-3
 */
public enum OpenTypeEnum {
	TAB("页签","TAB"),
	NEWWINDOW("新窗口","NEWWINDOW"),
	WINDOW("弹出窗口","WINDOW");
	private String label;
	private String value;
	private OpenTypeEnum(String label,String value){
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
		OpenTypeEnum[] ary = OpenTypeEnum.values();
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
