package com.wuyizhiye.basedata.portlet.service;


import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.portlet.model.PositionPortlet;

/**
 * @ClassName PositionPortletService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PositionPortletService extends BaseService<PositionPortlet> {
	public void saveAndUpdate(List<PositionPortlet> pplist);
	PositionPortlet getEntityByPositionId(String position);
}
