package com.wuyizhiye.basedata.portlet.model;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.portlet.enums.FormatSizeEnum;
import com.wuyizhiye.basedata.portlet.enums.LayoutEnum;
import com.wuyizhiye.basedata.portlet.enums.PortletStatusEnum;

/**
 * @ClassName HomeSet
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class HomeSet extends DataEntity {
	
	private LayoutEnum layout;
	private Integer isdefault;
	private PortletStatusEnum status;

	public String getLayoutStr(){
		return null==this.getLayout()?"":this.getLayout().getTitle();
	}
	
	public String getStatusStr(){
		return null==this.getStatus()?"":this.getStatus().getName();
	}

	public LayoutEnum getLayout() {
		return layout;
	}

	public void setLayout(LayoutEnum layout) {
		this.layout = layout;
	}

	public Integer getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Integer isdefault) {
		this.isdefault = isdefault;
	}

	public PortletStatusEnum getStatus() {
		return status;
	}

	public void setStatus(PortletStatusEnum status) {
		this.status = status;
	}
}
