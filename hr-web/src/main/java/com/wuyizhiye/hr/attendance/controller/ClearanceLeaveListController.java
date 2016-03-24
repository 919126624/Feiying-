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
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.workflow.enums.BillStatusEnum;
import com.wuyizhiye.hr.attendance.model.ClearanceLeave;
import com.wuyizhiye.hr.attendance.service.ClearanceLeaveService;
import com.wuyizhiye.hr.attendance.service.LeaveService;
import com.wuyizhiye.hr.enums.Ask4LeaveTypeEnum;
/**
 * 销假列表controller
 * @author hyl
 *
 * @since 2013-11-18
 */
@Controller
@RequestMapping(value="hr/clearanceLeave/*")
public class ClearanceLeaveListController extends ListController{
	@Autowired
	private ClearanceLeaveService clearanceLeaveService;
	@Autowired
	private LeaveService leaveService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		ClearanceLeave data = new ClearanceLeave();
		String leaveId = getString("leaveId");
		if(!StringUtils.isEmpty(leaveId)){
			data.setLeave(leaveService.getEntityById(leaveId));
		}
		return data;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		this.getRequest().setAttribute("ask4LeaveType", Ask4LeaveTypeEnum.values());//请假类型
		this.getRequest().setAttribute("leaveClearanceStatusEnum", BillStatusEnum.values());//销假状态
		return "hr/leave/clearanceLeaveList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		this.getRequest().setAttribute("ask4LeaveType", Ask4LeaveTypeEnum.values());//请假类型
		return "hr/leave/clearanceLeaveEdit";
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
		//请假类型
		config.registerJsonValueProcessor(Ask4LeaveTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Ask4LeaveTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((Ask4LeaveTypeEnum)value).getLabel());
					json.put("value", ((Ask4LeaveTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Ask4LeaveTypeEnum){
					return ((Ask4LeaveTypeEnum)value).getLabel();
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
		return "com.wuyizhiye.hr.attendance.dao.ClearanceLeaveDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return clearanceLeaveService;
	}

}
