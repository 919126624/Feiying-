package com.wuyizhiye.basedata.org.enums;

/**
 * @ClassName OrgTypeEnum
 * @Description 组织类型
 * @author li.biao
 * @date 2015-4-2
 */
public enum OrgTypeEnum {
	GROUP("集团","GROUP"),
	SON_GROUP("子集团","SON_GROUP"),
	VIRTUAL_CADRE("虚拟本部","VIRTUAL_CADRE"),
	COMPANY("子公司","COMPANY"),
	BRANCH_COMPANY("分公司","BRANCH_COMPANY"),
	STORE("分店","STORE"),
	OUTPOSTS("外派机构","OUTPOSTS"),
	NDEPENDENT_COMPANY("加盟机构","NDEPENDENT_COMPANY"),
	DEPT("部门","DEPT"),
	TEAM("分组","TEAM")
	;
	private String label;
	private String value;
	private OrgTypeEnum(String label,String value){
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
