package com.wuyizhiye.basedata.bank.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.bank.dao.CityDao;
import com.wuyizhiye.basedata.bank.model.City;
import com.wuyizhiye.basedata.bank.service.CityService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName CityServiceImpl
 * @Description 银行所属城市service
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="cityService")
@Transactional
public class CityServiceImpl extends DataEntityService<City> implements CityService {
	@Autowired
	private CityDao cityDao;
	@Override
	protected BaseDao getDao() {
		return cityDao;
	}

	@Override
	public List<City> getList(Map<String, Object> param) {
		return cityDao.getList(param);
	}

	@Override
	public List<City> getisModelList(String isModel) {
		Map<String, Object>param=new HashMap<String, Object>();
		param.put("isModel", isModel);
		return cityDao.getList(param);
	}
}
