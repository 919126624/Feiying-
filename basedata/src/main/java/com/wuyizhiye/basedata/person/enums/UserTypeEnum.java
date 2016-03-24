package com.wuyizhiye.basedata.person.enums;

/**
 * @ClassName UserTypeEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public enum UserTypeEnum {
	/*通用*/
	T01("管理员","T01"),
	T02("财务管理层","T02"),
	T03("财务人员","T03"),
	T04("HR管理层","T04"),
	T05("HR人员","T05"),
	
	/*盘客*/
	P01("业务员","P01"),
	P02("经理","P02"),
	P03("秘书","P03"),
	P04("业务线管理层","P04"),
	P05("业务线管理层助理","P05"),
	
	/*快销*/
	F01("销售员","F01"),
	F02("销售经理","F02"),
	F03("销售助理","F03"),
	F04("销售管理层","F04"),
	F05("新房销售管理层助理","F05"),
	OTHER("其他","OTHER")
	;
	
	private String label;
	private String value;
	private UserTypeEnum(String label,String value){
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
