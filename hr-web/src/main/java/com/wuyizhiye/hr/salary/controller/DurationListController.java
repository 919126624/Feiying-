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
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.hr.salary.model.Duration;
import com.wuyizhiye.hr.salary.service.DurationService;

/**
 * 薪酬期间controller
 * @author hyl
 *
 * @since 2014-02-12
 */
@Controller
@RequestMapping(value="hr/duration/*")
public class DurationListController extends ListController{
	@Autowired
	private DurationService durationService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return new Duration();
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "hr/salary/salarysetting/durationList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return "hr/salary/salarysetting/durationEdit";
	}	
	@RequestMapping(value="salarySettingView")
	public String leaveListView(){
		return "hr/salary/salarysetting/salarySettingView";
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
	/**
	 * 启用/禁用
	 * @param org 
	 * @param response
	 */
	@RequestMapping(value="enable")
	public void enable(Duration duration,HttpServletResponse response){
		Duration  old = durationService.getEntityById(duration.getId());
		old.setStatus(duration.getStatus());
		durationService.updateEntity(old);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.hr.salary.dao.DurationDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return durationService;
	}
}
