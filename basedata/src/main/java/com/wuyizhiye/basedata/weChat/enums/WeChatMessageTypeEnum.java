package com.wuyizhiye.basedata.weChat.enums;

/**
 * @ClassName WeChatMessageTypeEnum
 * @Description 消息提醒状态
 * @author li.biao
 * @date 2015-4-3
 */
public enum WeChatMessageTypeEnum {
	SEND("发送","SEND"),
	RECEIVE("接收","RECEIVE") ;
	private String label;
	private String value;
	private WeChatMessageTypeEnum(String label,String value){
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
