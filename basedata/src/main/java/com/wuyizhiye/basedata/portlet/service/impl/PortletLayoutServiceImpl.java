package com.wuyizhiye.basedata.portlet.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.portlet.dao.PortletLayoutDao;
import com.wuyizhiye.basedata.portlet.model.PortletLayout;
import com.wuyizhiye.basedata.portlet.service.PortletLayoutService;

/**
 * @ClassName PortletLayoutServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="portletLayoutService")
public class PortletLayoutServiceImpl extends BaseServiceImpl<PortletLayout> implements PortletLayoutService{

	@Autowired
	private PortletLayoutDao portletLayoutDao;

	@Override
	protected BaseDao getDao() {
		return portletLayoutDao;
	}

	@Override
	public void updateStatus(PortletLayout portlet) {
		this.portletLayoutDao.updateStatus(portlet);
	}

	@Override
	public void updateSelective(PortletLayout portlet) {
		this.portletLayoutDao.updateSelective(portlet);
	}

	@Override
	public void deleteByParent(String parentId) {
		this.portletLayoutDao.deleteByParent(parentId);
	}

	@Override
	public List<PortletLayout> getlist(Map<String,Object> map) {
		return this.portletLayoutDao.getlist(map);
	}
	

	
}
