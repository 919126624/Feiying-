package com.wuyizhiye.basedata.bank.dao.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.bank.dao.CityDao;
import com.wuyizhiye.basedata.bank.model.City;

/**
 * @ClassName CityDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="cityDao")
public class CityDaoImpl extends BaseDaoImpl implements CityDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.bank.dao.CityDao";
	}
	@Override
	public List<City> getList(Map<String, Object> param) {
		return this.getSqlSession().selectList(getNameSpace()+".select", param);
	}
}
