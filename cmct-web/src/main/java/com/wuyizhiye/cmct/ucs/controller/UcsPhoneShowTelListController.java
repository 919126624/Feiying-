package com.wuyizhiye.cmct.ucs.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.enums.StateEnum;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneShowTel;
import com.wuyizhiye.cmct.ucs.service.UcsPhoneShowTelService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName UcsPhoneShowTelListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/ucsPhoneShowTel/*")
public class UcsPhoneShowTelListController extends ListController {

	@Autowired
	private UcsPhoneShowTelService ucsPhoneShowTelService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return new UcsPhoneShowTel();
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "cmct/ucs/ucsPhoneShowTelList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		this.getRequest().setAttribute("nowDate",new Date());
		this.getRequest().setAttribute("name", getCurrentUser().getName());
		return "cmct/ucs/ucsPhoneShowTelEdit";
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.cmct.ucs.dao.UcsPhoneShowTelDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return ucsPhoneShowTelService;
	}
	
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		//日期
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
		//状态
		config.registerJsonValueProcessor(StateEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof StateEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((StateEnum)value).getName());
					json.put("value", ((StateEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof StateEnum){
					return ((StateEnum)value).getName();
				}
				return null;
			}
		});
		return config;
	}
}
