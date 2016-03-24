package com.wuyizhiye.basedata.remind.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.remind.model.Remind;
import com.wuyizhiye.basedata.remind.model.RemindManage;
import com.wuyizhiye.basedata.remind.service.RemindManageService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName RemindManageListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/remindManage/*")
public class RemindManageListController extends ListController{
	
	@Autowired
	private RemindManageService remindManageService;

	@Override
	protected CoreEntity createNewEntity() {
		return new RemindManage();
	}

	@Override
	protected String getListView() {
		Map<String,Object> param = new HashMap<String, Object>();
		List<Remind> remindList = queryExecutor.execQuery("com.wuyizhiye.basedata.remind.dao.RemindDao.select", param, Remind.class);
		List<Job> jobList = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobDao.select", param, Job.class);
		//param.put("job", jobList.get(0).getId());
		//List<JobLevel> jobLevelList = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobLevelDao.select", param, JobLevel.class);
		getRequest().setAttribute("jobList", jobList);
		//getRequest().setAttribute("jobLevelList", jobLevelList);
		getRequest().setAttribute("remindList", remindList);
		return "basedata/remind/remindManageList";
	}

	@Override
	protected String getEditView() {
		return "basedata/remind/remindManageEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.remind.dao.RemindManageDao.select";
	}

	@Override
	protected RemindManageService getService() {
		return remindManageService;
	}
	
	@RequestMapping(value="getRemindManageById")
	public void getRemindById(HttpServletResponse response) {
		String jobId = getString("id");
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("jobId", jobId);
		RemindManage remindManage = queryExecutor.execOneEntity("com.wuyizhiye.basedata.remind.dao.RemindManageDao.selectJobLevel", param, RemindManage.class);
		param.clear();
		param.put("id", jobId);
		List<Remind> tempList = queryExecutor.execQuery("com.wuyizhiye.basedata.remind.dao.RemindDao.selectByRemindManage", param, Remind.class);
		remindManage.setRemindList(tempList);
		
		getOutputMsg().put("remindManage", remindManage);
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="jobLevel")
	public void jobLevel(HttpServletResponse response){
		String job = getString("job");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("job", job);
		List<JobLevel> jobLevelList = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobLevelDao.select", param, JobLevel.class);
		getOutputMsg().put("jobLevelList", jobLevelList);
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="deleteById")
	public void deleteById(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		getService().deleteById(id);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "删除成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@Override
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.remind.dao.RemindManageDao.selectJobLevel", pagination, getListDataParam());
		List<RemindManage> remindList = (List<RemindManage>) pagination.getItems();
		for(int i = 0;i<remindList.size();i++){
			RemindManage temp = remindList.get(i);
			String jobId = temp.getJob().getId();
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("id", jobId);
			List<Remind> tempList = queryExecutor.execQuery("com.wuyizhiye.basedata.remind.dao.RemindDao.selectByRemindManage", param, Remind.class);
			remindList.get(i).setRemindList(tempList);
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
}
