package com.wuyizhiye.cmct.phone.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneDialLog;
import com.wuyizhiye.cmct.phone.service.PhoneDialLogService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneDialLogListController
 * @Description 话费管理-话单明细
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneDialLog/*")
public class PhoneDialLogListController extends ListController{
	private static Log log = LogFactory.getLog(PhoneDialLogListController.class);
	
	@Autowired
	private PhoneDialLogService phoneDialLogService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return new PhoneDialLog();
	}
	
	@Override
	protected String getListView() {
		return "cmct/phone/phoneDialLogList";
	}
	
	@Override
	protected String getEditView() {
		return "cmct/phone/phoneDialLogView";
	}
	
	@Override
	protected String getListMapper() {
		return PhoneDialLog.MAPPER+".select";
	}
	
	@Override
	protected BaseService getService() {
		return phoneDialLogService;
	}
	
	/**
	 * json转换config
	 * @return
	 */
	@Override
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
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
}
