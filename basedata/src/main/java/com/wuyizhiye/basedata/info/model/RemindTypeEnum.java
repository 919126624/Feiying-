package com.wuyizhiye.basedata.info.model;

/**
 * @ClassName RemindTypeEnum
 * @Description 读取方式
 * @author li.biao
 * @date 2015-4-2
 */
public enum RemindTypeEnum {
	NOPROMPT("不提示","NOPROMPT"),
	FLOAT_BOX("浮框提示","FLOAT_BOX"),
	FLOAT_BOX_ED("已浮框提示","FLOAT_BOX_ED"),
	POPUP("弹窗提示","POPUP"),
	POPUP_ED("已弹窗提示","POPUP_ED");
	
	private String label;
	private String value;
	private RemindTypeEnum(String label,String value){
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
}
