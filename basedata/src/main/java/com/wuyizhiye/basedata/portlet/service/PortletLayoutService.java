package com.wuyizhiye.basedata.portlet.service;


import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.portlet.model.PortletLayout;

/**
 * @ClassName PortletLayoutService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PortletLayoutService extends BaseService<PortletLayout> {
	void updateStatus(PortletLayout portlet);
	void updateSelective(PortletLayout portlet);
	void deleteByParent(String parentId);
	List<PortletLayout> getlist(Map<String,Object> map);
}
