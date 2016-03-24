package com.wuyizhiye.basedata.code.enums;

/**
 * @ClassName AreaTypeEnum
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public enum AreaTypeEnum {
	CITY("城市",1),//城市
	REGION("大片区",2),//大片区
	GEOGRAPHY("地理片区",3), //地理片区
	BUSINESS("商业片区",4), //商业片区
	ROAD("关键路段",5); //路段
	
	private String desc;
	private int value;

	private AreaTypeEnum(String desc, int value){
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
	
	public static AreaTypeEnum getEnum(int value){
		AreaTypeEnum resultEnum=null;
		AreaTypeEnum[] enumAry=AreaTypeEnum.values();
		for(int i=0;i<enumAry.length;i++){
			if(enumAry[i].getValue()==value){
				resultEnum=enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
}
