package com.wuyizhiye.hr.salary.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.hr.salary.model.Rate;
import com.wuyizhiye.hr.salary.service.RateService;

/**
 * 薪酬期间controller
 * @author hyl
 *
 * @since 2014-02-12
 */
@Controller
@RequestMapping(value="hr/rate/*")
public class RateListController extends ListController{
	@Autowired
	private RateService reateService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return new Rate();
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "hr/salary/salarysetting/rateList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return "hr/salary/salarysetting/rateEdit";
	}	
	/**
	 * json转换config
	 * @return
	 */
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		//日期
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		return config;
	}
	
	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.hr.salary.dao.RateDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return reateService;
	}
}
