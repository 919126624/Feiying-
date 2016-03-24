package com.wuyizhiye.basedata.org.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.PositionDao;
import com.wuyizhiye.basedata.org.model.Position;

/**
 * @ClassName PositionDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="positionDao")
public class PositionDaoImpl extends BaseDaoImpl implements PositionDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Position> getByOrg(String org) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("belongOrg", org);
		return getSqlSession().selectList(getNameSpace() + ".select", param);
	}

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.org.dao.PositionDao";
	}
}
