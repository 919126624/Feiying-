package com.wuyizhiye.hr.attendance.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.hr.affair.model.EmployeeOrientation;
import com.wuyizhiye.hr.attendance.model.AttendanceRule;
import com.wuyizhiye.hr.attendance.service.AttendanceRuleService;
import com.wuyizhiye.hr.enums.AttendanceRuleTypeEnum;
import com.wuyizhiye.hr.enums.BillStatusEnum;


/**
 * 考勤规则
 * @author ouyangyi
 * @since 2013-11-20 上午10:44:02
 */
@Controller
@RequestMapping(value="hr/attendanceRule/*")
public class AttendanceRuleListController extends ListController{
	 
	@Resource
	private AttendanceRuleService attendanceRuleService ;
	

	@Override
	protected String getListMapper() {
		 
		return "com.wuyizhiye.hr.attendance.dao.AttendanceRuleDao.getAttendanceRuleByCond";
	}

	@Override
	protected BaseService getService() {
		 
		return attendanceRuleService;
	}

	@Override
	protected CoreEntity createNewEntity() {
		 AttendanceRule attendanceRule = new AttendanceRule(); 
		return attendanceRule;
	}

	@Override
	protected String getListView() {
	    getRequest().setAttribute("ruleTypes", AttendanceRuleTypeEnum.toList());
		return "hr/attendance/ruleManage";
	}

	@Override
	protected String getEditView() {
		 
		return null;
	}
	
	@RequestMapping(value="getAttendanceRuleById")
	public void cancleBill(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		 
		AttendanceRule data =  attendanceRuleService.getEntityById(getString("id"));
		getOutputMsg().put("data", data);
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}

}
