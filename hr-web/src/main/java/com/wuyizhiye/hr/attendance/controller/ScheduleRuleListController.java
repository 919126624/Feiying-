package com.wuyizhiye.hr.attendance.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.hr.attendance.model.ScheduleRule;
import com.wuyizhiye.hr.attendance.service.ScheduleRuleService;
/**
 * 排班规则controller
 * @author hyl
 *
 * @since 2013-11-25
 */
@Controller
@RequestMapping(value="hr/scheduleRule/*")
public class ScheduleRuleListController extends ListController{
	@Autowired
	private ScheduleRuleService scheduleRuleService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return new ScheduleRule();
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "hr/schedulerule/scheduleRuleList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return "hr/schedulerule/scheduleRuleEdit";
	}	
	@RequestMapping(value="leaveListView")
	public String leaveListView(){
		return "hr/leaverule/leaveRuleListView";
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
		return "com.wuyizhiye.hr.attendance.dao.ScheduleRuleDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return scheduleRuleService;
	}

}
