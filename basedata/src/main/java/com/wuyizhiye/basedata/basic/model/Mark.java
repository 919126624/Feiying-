package com.wuyizhiye.basedata.basic.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName Mark
 * @Description 标签类型
 * @author li.biao
 * @date 2015-4-2
 */
public class Mark extends CoreEntity {

	private static final long serialVersionUID = 1L;
	public static final String NUMBER_M = "M" ;
	public static final String NUMBER_S = "S" ;

	public static final String MAPPER = "com.wuyizhiye.basedata.basic.dao.MarkDao";
	
	//标签名
	private String name ;
	
	//引用模块
	private String module ;
	
	//父类型
	private Mark parent ;
	
	//隐/显（0：表示系统生成、隐式；1：表示用户定义、显示）
	private int display ;
	
	//系统级（0：系统级:；1：用户级）
	private int system ;
	
	//编码
	private String number ;
	
	//长编码
	private String longNumber ;
	
	//长名字
	private String longName ;
	
	//非表中字段
	private String pid;
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getLongNumber() {
		return longNumber;
	}

	public void setLongNumber(String longNumber) {
		this.longNumber = longNumber;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public int getSystem() {
		return system;
	}

	public void setSystem(int system) {
		this.system = system;
	}

	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Mark getParent() {
		return parent;
	}

	public void setParent(Mark parent) {
		this.parent = parent;
	}
	
	
}
