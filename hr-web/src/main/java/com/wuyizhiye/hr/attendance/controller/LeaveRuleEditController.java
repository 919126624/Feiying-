/**
 * com.wuyizhiye.basedata.basic.controller.BasicDataEditController.java
 */
package com.wuyizhiye.hr.attendance.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.access.model.MacAddress;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.util.BeanUtils;
import com.wuyizhiye.hr.affair.model.EmployeeRunDiskBill;
import com.wuyizhiye.hr.attendance.model.Leave;
import com.wuyizhiye.hr.attendance.model.LeaveRule;
import com.wuyizhiye.hr.attendance.service.LeaveRuleService;
import com.wuyizhiye.hr.utils.BaseUtils;


/**
 * 请假规则controller
 * @author lambert.huang
 *
 * @since 2013-11-18
 */
@Controller
@RequestMapping(value="hr/leaveRule/*")
public class LeaveRuleEditController extends EditController {
	@Autowired
	private LeaveRuleService leaveRuleService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private QueryExecutor queryExecutor;
	@Override
	protected Class<LeaveRule> getSubmitClass() {
		return LeaveRule.class;
	}

	@Override
	protected BaseService<LeaveRule> getService() {
		return this.leaveRuleService;
	}

	@Override
	protected LeaveRule getSubmitEntity() {
		LeaveRule data = (LeaveRule) super.getSubmitEntity();
		return data;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="save")
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		LeaveRule data = getSubmitEntity();
		if(validate(data)){
			if(data instanceof LeaveRule){
				if(StringUtils.isEmpty(((LeaveRule)data).getId())){
					data.setCreateTime(new Date());
					data.setCreator(getCurrentUser());
					getService().addEntity((LeaveRule)data);
				}else{
					data.setLastUpdateTime(new Date());
					data.setUpdator(getCurrentUser());
					getService().updateEntity((LeaveRule)data);
				}
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	@RequestMapping(value="enable",method=RequestMethod.POST)
	public void enable(@RequestParam(value="id")String id,@RequestParam(value="enable")String enable,HttpServletResponse response){
		 LeaveRule leaveRule = getService().getEntityById(id);
		if(leaveRule != null){
			leaveRule.setEnable("1".equals(enable)?true:false);
			getService().updateEntity(leaveRule);
			getOutputMsg().put("STATE", "SUCCESS");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "数据不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	/**
	 * 判断该组织规则是否已经存在
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="judgeOrgId")
	public void judgeOrgId(HttpServletRequest request, HttpServletResponse response){
		String id=this.getString("orgId");
		String neqId = this.getString("id");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orgId", id);
		param.put("neqId", neqId);
		int count = queryExecutor.execCount("com.wuyizhiye.hr.attendance.dao.LeaveRuleDao.judgeOrgId", param);
		if(count > 0){
			this.getOutputMsg().put("MSG", "该组织规则已经存在!");
		} else {
			this.getOutputMsg().put("MSG", null);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	/**
	 * 获取所属组织的请假规则，若该组织无此规则，
	 * 则查找其上一级组织的请假规则
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getRule")
	public void getRule(HttpServletRequest request, HttpServletResponse response){
		String orgId=this.getString("orgId");
		LeaveRule data=null;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orgLongNumber", orgService.getEntityById(orgId).getLongNumber());
		List<LeaveRule> leaveRuleList=queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.LeaveRuleDao.getByOrgLongNumber", param, LeaveRule.class);
		if(leaveRuleList.size()>0){
			data=leaveRuleList.get(0);
		}
		outPrint(response, JSONObject.fromObject(data));
	}
	/**
	 * 获取上级请假规则
	 */
public LeaveRule getLeaveRule(String orgId){
	LeaveRule data=null;
	data=leaveRuleService.getEntityByOrgId(orgId);
	if(data==null){
		Org org = orgService.getEntityById(orgId);
		if(org.getParent().getId()!=null){
			return getLeaveRule(org.getParent().getId());
		}
	}
	return data;
}
}
