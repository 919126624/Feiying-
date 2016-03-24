/**
 * com.wuyizhiye.basedata.basic.controller.BasicDataEditController.java
 */
package com.wuyizhiye.hr.attendance.controller;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.EditController;
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
public class ScheduleRuleEditController extends EditController {
	@Autowired
	private ScheduleRuleService scheduleRuleService;
	@Autowired
	private QueryExecutor queryExecutor;
	@Override
	protected Class<ScheduleRule> getSubmitClass() {
		return ScheduleRule.class;
	}

	@Override
	protected BaseService<ScheduleRule> getService() {
		return this.scheduleRuleService;
	}

	@Override
	protected ScheduleRule getSubmitEntity() {
		ScheduleRule data = (ScheduleRule) super.getSubmitEntity();
		return data;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="save")
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		ScheduleRule data = getSubmitEntity();
		if(validate(data)){
			if(data instanceof ScheduleRule){
				if(StringUtils.isEmpty(((ScheduleRule)data).getId())){
					data.setCreateTime(new Date());
					data.setCreator(getCurrentUser());
					getService().addEntity((ScheduleRule)data);
				}else{
					data.setLastUpdateTime(new Date());
					data.setUpdator(getCurrentUser());
					getService().updateEntity((ScheduleRule)data);
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
		ScheduleRule leaveRule = getService().getEntityById(id);
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
	
}
