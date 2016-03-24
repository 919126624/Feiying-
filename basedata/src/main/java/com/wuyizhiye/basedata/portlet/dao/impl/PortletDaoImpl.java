package com.wuyizhiye.basedata.portlet.dao.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.portlet.dao.PortletDao;
import com.wuyizhiye.basedata.portlet.model.Portlet;

/**
 * @ClassName PortletDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="portletDao")
public class PortletDaoImpl extends BaseDaoImpl implements PortletDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.portlet.dao.PortletDao";
	}

	@Override
	public void updateStatus(Portlet portal) {
		this.getSqlSession().update(getNameSpace()+".updateStatus", portal);
	}

	@Override
	public void updateSelective(Portlet portal) {
		this.getSqlSession().update(getNameSpace()+".updateSelective", portal);
	}
	@Override
	public List<Portlet> getList(Map<String, Object> param) {
		return this.getSqlSession().selectList(getNameSpace()+".select", param);
	}

	@Override
	public List<Portlet> getListForSync(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return this.getSqlSession().selectList(getNameSpace()+".selectForSync", param);
	}
}
