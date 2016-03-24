package com.wuyizhiye.basedata.portlet.enums;

/**
 * @ClassName FormatSizeEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public enum FormatSizeEnum {
	ONEXONE("1X1","ONEXONE"),
	ONEXTWO("1X2","ONEXTWO");
	private String title;
	private String value;
	private FormatSizeEnum(String title,String value){
		this.title = title;
		this.value = value;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
