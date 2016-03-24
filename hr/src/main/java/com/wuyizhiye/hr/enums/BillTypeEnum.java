package com.wuyizhiye.hr.enums;

/**
 * 单据类别
 * @author hlz
 *
 */
public enum BillTypeEnum {
	PERSONNEL("人事类","PERSONNEL"),
	ATTENDANCE("考勤类","ATTENDANCE"),
	REIMBURSEMENT("报销类","REIMBURSEMENT");
	
	private String name;
	
	private String value;
	
	private BillTypeEnum(String name,String value){
		this.name = name;
		this.value = value;
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
