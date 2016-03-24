package com.wuyizhiye.basedata.bank.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.bank.model.City;

/**
 * @ClassName CityService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface CityService extends BaseService<City> {

	public List<City> getList(Map<String, Object> param);
	
	public List<City>getisModelList(String isModel);
}
