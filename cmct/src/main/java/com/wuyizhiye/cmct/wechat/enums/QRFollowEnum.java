package com.wuyizhiye.cmct.wechat.enums;

/**
 * @ClassName QRFollowEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public enum QRFollowEnum {
	isAttention("关注","isAttention"),
	noAttention("取消关注","noAttention");
	
	private String name;
	private String value;
	
	private QRFollowEnum (String name,String value){
		this.name=name;
		this.value=value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
