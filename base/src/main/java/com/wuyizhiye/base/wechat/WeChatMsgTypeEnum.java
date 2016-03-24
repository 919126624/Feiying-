package com.wuyizhiye.base.wechat;

/**
 * @ClassName WeChatMsgTypeEnum
 * @Description 微信消息类型枚举
 * @author li.biao
 * @date 2015-4-1
 */
public enum WeChatMsgTypeEnum {
	/**
	 * 消息
	 */
	text("文本","text"),
	/**
	 * 消息
	 */
	news("消息","news") ;
	
	private String name;
	private String value;
	
	private WeChatMsgTypeEnum(String name,String value){
		this.setName(name);
		this.setValue(value);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}
