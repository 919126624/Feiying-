package com.wuyizhiye.cmct.sms.enums;

/**
 * @ClassName CustomerRelationEnum
 * @Description 客户关系
 * @author li.biao
 * @date 2015-5-26
 */
public enum CustomerRelationEnum {
	SELF("本人","本人","SELF"), //本人
	HUSBAND("丈夫","丈夫","HUSBAND"), //丈夫
	WIFE("妻子","妻子","WIFE"), //妻子
	KIN("亲属","亲属","KIN"), //亲属
	FRIEND("朋友","朋友","FRIEND"), //朋友
	SON("儿子","儿子","SON"), //儿子 
	OTHER("其他","其他","OTHER"), //其他
	CLASSMATE("同学","同学","CLASSMATE"); //同学
	
	private String name;
	private String value;
	private String desc;
	private CustomerRelationEnum(String name,String desc,String value){
		this.setName(name);
		this.setValue(value);
		this.setDesc(name);
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * 取枚举的json字符串
	 * @return
	 */
	public static String getJsonStr(){
		CustomerRelationEnum[] enums = CustomerRelationEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (CustomerRelationEnum enumTemp : enums) {
			if(!"[".equals(jsonStr.toString())){
				jsonStr.append(",");
			}
			jsonStr.append("{id:'")
					.append(enumTemp.getValue())
					.append("',name:'")
					.append(enumTemp.getName())
					.append("',desc:'")
					.append(enumTemp.getDesc())
					.append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
	
	
}
