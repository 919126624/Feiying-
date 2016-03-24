package com.wuyizhiye.basedata.portlet.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.portlet.dao.PortletDao;
import com.wuyizhiye.basedata.portlet.model.Portlet;
import com.wuyizhiye.basedata.portlet.service.PortletService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName PortletServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="portletService")
public class PortletServiceImpl extends DataEntityService<Portlet> implements PortletService{

	@Autowired
	private PortletDao portletDao;

	@Override
	protected BaseDao getDao() {
		return portletDao;
	}

	@Override
	public void updateStatus(Portlet portlet) {
		this.portletDao.updateStatus(portlet);
	}

	@Override
	public void updateSelective(Portlet portlet) {
		this.portletDao.updateSelective(portlet);
	}

	@Override
	public List<Portlet> getList(Map<String, Object> param) {
		return portletDao.getList(param);
	}

	@Override
	public List<Portlet> getListForSync(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return portletDao.getListForSync(param);
	}

	
}
