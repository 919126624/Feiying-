package com.wuyizhiye.basedata.portlet.dao.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.portlet.dao.HomeSetDao;
import com.wuyizhiye.basedata.portlet.model.HomeSet;

/**
 * @ClassName HomeSetDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="homeSetDao")
public class HomeSetDaoImpl extends BaseDaoImpl implements HomeSetDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.portlet.dao.HomeSetDao";
	}

	@Override
	public void updateStatus(HomeSet homeset) {
		this.getSqlSession().update(getNameSpace()+".updateStatus", homeset);
	}

	@Override
	public void updateSelective(HomeSet homeset) {
		this.getSqlSession().update(getNameSpace()+".updateSelective", homeset);
	}

	@Override
	public List<HomeSet> querylist(Map<String, Object> map) {
		return this.getSqlSession().selectList(getNameSpace()+".select", map);
	}

}
