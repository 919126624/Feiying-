package com.wuyizhiye.base.jpush.push;

/***
 * 定义推送设备的标签
 * @author caiying
 * @date 2016-1-6
 *
 */
public enum JpushTagEnums {
	ANDROID("安卓设备","ANDROID"),
	IOS("IOS设备","IOS"),
	DEV("开发环境","DEV"),
	TESTING("测试环境","TESTING"),
	GENERATE("生产环境","GENERATE"),
	ALL("所有设备","ALL");
	
	private String name;
	private String value;
	
	private JpushTagEnums(String name ,String value){
		this.setName(name);
		this.setValue(value);
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
