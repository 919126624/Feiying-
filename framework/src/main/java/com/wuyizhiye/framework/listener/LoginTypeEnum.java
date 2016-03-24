package com.wuyizhiye.framework.listener;

/**
 * @ClassName LoginTypeEnum
 * @Description 登陆类型  
 * @author li.biao
 * @date 2015-4-7
 */
public enum LoginTypeEnum {
	PC("计算机登陆",1),
	MOBILE("移动设备登陆",2); 
	private String desc;
	private int value;

	private LoginTypeEnum(String desc, int value){
		this.desc = desc;
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public static LoginTypeEnum getEnum(int value){
		LoginTypeEnum resultEnum=null;
		LoginTypeEnum[] enumAry=LoginTypeEnum.values();
		for(int i=0;i<enumAry.length;i++){
			if(enumAry[i].getValue()==value){
				resultEnum=enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
}
