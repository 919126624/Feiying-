package com.wuyizhiye.basedata.portlet.dao.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.portlet.dao.PortletLayoutDao;
import com.wuyizhiye.basedata.portlet.model.PortletLayout;

/**
 * @ClassName PortletLayoutDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="portletLayoutDao")
public class PortletLayoutDaoImpl extends BaseDaoImpl implements PortletLayoutDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.portlet.dao.PortletLayoutDao";
	}

	@Override
	public void updateStatus(PortletLayout portal) {
		this.getSqlSession().update(getNameSpace()+".updateStatus", portal);
	}

	@Override
	public void updateSelective(PortletLayout portal) {
		this.getSqlSession().update(getNameSpace()+".updateSelective", portal);
	}

	@Override
	public void deleteByParent(String parentId) {
		this.getSqlSession().delete(getNameSpace()+".deleteByParent", parentId);
	}

	@Override
	public List<PortletLayout> getlist(Map<String,Object> map) {
		return this.getSqlSession().selectList(getNameSpace()+".select", map);
	}

}
