package com.wuyizhiye.hr.attendance.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.util.BeanUtils;
import com.wuyizhiye.hr.attendance.model.AttendanceRule;
import com.wuyizhiye.hr.attendance.service.AttendanceRuleService;

/**
 * 考勤规则
 * @author ouyangyi
 * @since 2013-11-20 上午10:44:56
 */
@Controller
@RequestMapping(value="hr/attendanceRule/*")
public class AttendanceRuleEditController extends EditController{
	

	@Resource
	private AttendanceRuleService attendanceRuleService ;

	@Override
	protected Class getSubmitClass() {
		 
		return AttendanceRule.class;
	}

	@Override
	protected AttendanceRuleService getService() {
		return attendanceRuleService;
	}
	
	
	protected Object getSubmitEntity(){
		Class c = getSubmitClass();
		if(c==null){
			return null;
		}
		String id = getString("id");
		AttendanceRule entity = null;
		Date now = new Date(); 
		if(!StringUtils.isEmpty(id)){
			entity = attendanceRuleService.getEntityById(id);
		}else{
			entity = new AttendanceRule();
			entity.setCreateTime(now);
			entity.setCreator(SystemUtil.getCurrentUser());
		}
		entity.setUpdator(SystemUtil.getCurrentUser());
		entity.setLastUpdateTime(now);
		entity = BeanUtils.fillentity(getParamMap(),entity, c);
		String reminderTime = entity.getReminderTime();
		if(StringUtils.isNotNull(reminderTime)){
			int hour = Integer.valueOf(reminderTime.split(":")[0]);
			int minute = Integer.valueOf(reminderTime.split(":")[1]);
			entity.setHour(hour);
			entity.setMinute(minute);
		}
		return entity;
	}
	
	protected boolean validate(Object data){
		AttendanceRule entity = (AttendanceRule)data;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgId", entity.getOrg().getId());
		params.put("idNotEq", entity.getId());
		List<AttendanceRule> attendanceRules = this.queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceRuleDao.getAttendanceRuleByCond",params, AttendanceRule.class);
		if(attendanceRules.size()>0){
			//该组织已存在考勤规则
			getOutputMsg().put("STATE", "EXISTED");
			return false;
		}
		
		return true;
	}
	
}
