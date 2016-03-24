package com.wuyizhiye.hr.attendance.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.workflow.enums.BillStatusEnum;
import com.wuyizhiye.hr.attendance.model.OverTime;
import com.wuyizhiye.hr.attendance.service.OverTimeService;
/**
 * 加班申请列表controller
 * @author hyl
 *
 * @since 2013-11-18
 */
@Controller
@RequestMapping(value="hr/overTime/*")
public class OverTimeListController extends ListController{
	@Autowired
	private OverTimeService overTimeService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return new OverTime();
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		this.getRequest().setAttribute("BillStatus", BillStatusEnum.values());//单据状态
		return "hr/overtime/overTimeList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return "hr/overtime/overTimeEdit";
	}	
	/**
	 * json转换config
	 * @return
	 */
	@RequestMapping(value="add")
	@Dependence(method="list")
	public String add(ModelMap model){
		model.put("data", createNewEntity());		
		return getEditView();
	}
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
		//单据状态
		config.registerJsonValueProcessor(BillStatusEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof BillStatusEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((BillStatusEnum)value).getLabel());
					json.put("value", ((BillStatusEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof BillStatusEnum){
					return ((BillStatusEnum)value).getLabel();
				}
				return null;
			}
		});
		return config;
	}
	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.hr.attendance.dao.OverTimeDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return overTimeService;
	}

}
