package com.wuyizhiye.basedata.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.bank.model.City;
import com.wuyizhiye.basedata.bank.service.CityService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName CityEditController
 * @Description 城市编辑controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/cityEdit/*")
public class CityEditController extends EditController {
	@Autowired
	private CityService cityService;
	@Override
	protected Class<City> getSubmitClass() {
		return City.class;
	}
	@Override
	protected Object getSubmitEntity() {
		City city = (City) super.getSubmitEntity();
		return city;
	}
	@Override
	protected BaseService<City> getService() {
		return cityService;
	}
}
