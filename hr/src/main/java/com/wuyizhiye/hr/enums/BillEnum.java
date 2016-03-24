package com.wuyizhiye.hr.enums;

/**
 * 单据
 * @author hlz
 *
 */
public enum BillEnum {
	//人事事务
	RUNDISK(BillTypeEnum.PERSONNEL,"新增跑盘","RUNDISK"),
	DELRUNDISK(BillTypeEnum.PERSONNEL,"删除跑盘","DELRUNDISK"),
	ENROLL(BillTypeEnum.PERSONNEL,"入职","ENROLL"),
	TRANSFER(BillTypeEnum.PERSONNEL,"调职","TRANSFER"),
	PROMOTION(BillTypeEnum.PERSONNEL,"晋升","PROMOTION"),
	DEMOTION(BillTypeEnum.PERSONNEL,"降职","DEMOTION"),
	INCREASE_PARTTIMEJOB(BillTypeEnum.PERSONNEL,"兼职","INCREASE_PARTTIMEJOB"),
	DISMISS_PARTTIMEJOB(BillTypeEnum.PERSONNEL,"撤职","DISMISS_PARTTIMEJOB"),
	LEAVE(BillTypeEnum.PERSONNEL,"离职","LEAVE"),
	POSITIVE(BillTypeEnum.PERSONNEL,"转正","POSITIVE"),
	REINSTATEMENT(BillTypeEnum.PERSONNEL,"复职","REINSTATEMENT");
	
	private BillTypeEnum parent;
	private String label;
	private String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private BillEnum(BillTypeEnum parent,String label,String value){
		this.label = label;
		this.parent = parent;
		this.value = value;
	}
	
	public BillTypeEnum getParent() {
		return parent;
	}

	public void setParent(BillTypeEnum parent) {
		this.parent = parent;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
