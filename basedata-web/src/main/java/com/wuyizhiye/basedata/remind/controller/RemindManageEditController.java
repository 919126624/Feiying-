package com.wuyizhiye.basedata.remind.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.remind.model.Remind;
import com.wuyizhiye.basedata.remind.model.RemindManage;
import com.wuyizhiye.basedata.remind.service.RemindManageService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName RemindManageEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/remindManage/*")
public class RemindManageEditController extends EditController{
	
	@Autowired
	private RemindManageService remindManageService;

	@Override
	protected Class<RemindManage> getSubmitClass() {
		return RemindManage.class;
	}

	@Override
	protected BaseService<RemindManage> getService() {
		return remindManageService;
	}

	@Override
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException {
		String remindIdStr = getString("remindIdStr");
		String jobId = getString("jobId");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("jobId", jobId);
		int count = queryExecutor.execOneEntity("com.wuyizhiye.basedata.remind.dao.RemindManageDao.selectCount", param, Integer.class);
		if(count>0){
			getOutputMsg().put("STATE", "FAILURE");
			getOutputMsg().put("MSG", "该岗位职级已经有相关提醒，请使用修改功能");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
			return;
		}
		String[] split = remindIdStr.split(",");
		for(String remindId : split){
			RemindManage remindManage = new RemindManage();
			Job job = new Job();
			job.setId(jobId);
			Remind remind = new Remind();
			remind.setId(remindId);
			remindManage.setUUID();
			remindManage.setRemind(remind);
			remindManage.setJob(job);
			remindManageService.addEntity(remindManage);
		}
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="update")
	public void update(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String id = getString("id");
		String remindIdStr = getString("remindIdStr");
		String jobId = getString("jobId");
		if(!StringUtils.isEmpty(id)){
			remindManageService.deleteById(jobId);
		}
		String[] split = remindIdStr.split(",");
		for(String remindId : split){
			RemindManage remindManage = new RemindManage();
			Job job = new Job();
			job.setId(jobId);
			Remind remind = new Remind();
			remind.setId(remindId);
			remindManage.setUUID();
			remindManage.setRemind(remind);
			remindManage.setJob(job);
			remindManageService.addEntity(remindManage);
		}
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

}
