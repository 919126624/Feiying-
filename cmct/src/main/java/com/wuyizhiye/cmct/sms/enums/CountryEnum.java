package com.wuyizhiye.cmct.sms.enums;

/**
 * @ClassName CountryEnum
 * @Description 国籍
 * @author li.biao
 * @date 2015-5-26
 */
public enum CountryEnum {
	MAINLAND("大陆","MAINLAND","大陆"), //大陆
	HONGKONG("香港","HONGKONG","香港"),  //香港
	OVERSEA("海外","OVERSEA","海外"),  //海外
	TAIWAN("台湾","TAIWAN","台湾"),  //台湾
	AOMEN("澳门","AOMEN","澳门"); //澳门
	
	private String name;
	private String value;
	private String desc;
	private CountryEnum(String name,String value,String desc){
		this.setName(name);
		this.setValue(value);
		this.setDesc(desc);
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
		CountryEnum[] enums = CountryEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (CountryEnum enumTemp : enums) {
			if(!"[".equals(jsonStr.toString())){
				jsonStr.append(",");
			}
			jsonStr.append("{id:'")
					.append(enumTemp.getValue())
					.append("',name:'")
					.append(enumTemp.getName())
					.append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
	
}
