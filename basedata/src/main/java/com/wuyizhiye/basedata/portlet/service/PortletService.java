package com.wuyizhiye.basedata.portlet.service;


import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.portlet.model.Portlet;

/**
 * @ClassName PortletService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PortletService extends BaseService<Portlet> {
	void updateStatus(Portlet portlet);
	void updateSelective(Portlet portlet);
	public List<Portlet> getList(Map<String, Object> param);
	public List<Portlet> getListForSync(Map<String, Object> param);
}
