package com.wuyizhiye.basedata.portlet.dao.impl;


import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.portlet.dao.PositionPortletDao;
import com.wuyizhiye.basedata.portlet.model.PositionPortlet;

/**
 * @ClassName PositionPortletDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="positionPortletDao")
public class PositionPortletDaoImpl extends BaseDaoImpl implements PositionPortletDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.portlet.dao.PositionPortletDao";
	}

	@Override
	public PositionPortlet getEntityByPositionId(String position) {
		return (PositionPortlet)this.getSqlSession().selectOne(this.getNameSpace()+".getByPosition", position);
	}

	
}
