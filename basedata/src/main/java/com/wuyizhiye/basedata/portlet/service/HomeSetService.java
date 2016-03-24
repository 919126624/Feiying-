package com.wuyizhiye.basedata.portlet.service;


import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.portlet.model.HomeSet;
import com.wuyizhiye.basedata.portlet.model.PortletLayout;

/**
 * @ClassName HomeSetService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface HomeSetService extends BaseService<HomeSet> {
	void updateStatus(HomeSet portlet);
	void updateSelective(HomeSet portlet);
	void addHomeSet(HomeSet h,List<PortletLayout> plist,boolean add);
	void updateDefault(HomeSet portlet);
}
