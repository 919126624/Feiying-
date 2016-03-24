package com.wuyizhiye.basedata.remind.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName Remind
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class Remind extends DataEntity{

	private static final long serialVersionUID = 1L;

	private String remark;//提醒描述
	private String url;//提醒项方法
	
	private Integer show;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getShow() {
		return show;
	}
	public void setShow(Integer show) {
		this.show = show;
	}

	
}
