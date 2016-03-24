package com.wuyizhiye.basedata.topic.enums;

/**
 * @ClassName TopicStatusEnum
 * @Description 话题状态
 * @author li.biao
 * @date 2015-4-3
 */
public enum TopicStatusEnum {
	STATE_1("未处理","STATE_1"),
	STATE_2("处理中","STATE_2"),
	STATE_3("已解决","STATE_3"),
	STATE_4("已采纳","STATE_4"),
	STATE_5("谢谢你","STATE_5"),
	STATE_6("金点子","STATE_6"),
	
	STATE_B("己提交","STATE_B"),
//	STATE_C("处理中","STATE_C"),
	STATE_W("待处理","STATE_W"),
	STATE_D("己处理","STATE_D"),
	STATE_T("转交","STATE_T"),
	STATE_E("己关闭","STATE_E"),
	
//	STATE_E("己关闭","STATE_E"),
	;
	private String label;
	private String value;
	private TopicStatusEnum(String label,String value){
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
