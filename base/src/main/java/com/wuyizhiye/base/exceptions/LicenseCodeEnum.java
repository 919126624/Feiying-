package com.wuyizhiye.base.exceptions;

/**
 * @ClassName LicenseCodeEnum
 * @Description 许可异常枚举
 * @author li.biao
 * @date 2015-4-1
 */
public enum LicenseCodeEnum {
	
	NOPERMS("未获软件许可"),
	NOTOPERMS("未到软件许可时间"),
	OUTOFPERMS("超出软件许可期"),
	OUTUSER("超出软件许可在线人数");
	
	private String name;
	
	private LicenseCodeEnum(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
