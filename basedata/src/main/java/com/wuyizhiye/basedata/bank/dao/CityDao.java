package com.wuyizhiye.basedata.bank.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.bank.model.City;

/**
 * @ClassName CityDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface CityDao  extends BaseDao{

	public List<City> getList(Map<String, Object> param);
}
