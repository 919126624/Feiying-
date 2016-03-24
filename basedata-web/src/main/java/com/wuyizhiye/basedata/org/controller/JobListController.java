package com.wuyizhiye.basedata.org.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.enums.WorkBenchTypeEnum;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.model.WeixinOrg;
import com.wuyizhiye.basedata.org.service.JobCategoryService;
import com.wuyizhiye.basedata.org.service.JobService;
import com.wuyizhiye.basedata.org.service.WeixinOrgService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName JobListController
 * @Description 岗位列表controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/job/*")
public class JobListController extends ListController {
	@Autowired
	private JobService jobService;
	@Autowired
	private JobCategoryService jobCategoryService;
	@Autowired
	private WeixinOrgService weixinOrgService;
	@Override
	protected CoreEntity createNewEntity() {
		Job data=new Job();
		String type = getString("type");
		if(!StringUtils.isEmpty(type)){
			data.setJobCategory(jobCategoryService.getEntityById(type));
		}
		return data;
	}
	@Override
	protected String getListView() {
		this.getRequest().setAttribute("isPermission", this.hasPermission("b9d274a7-c800-4dbe-94e6-5311a346ccec"));//是否可以授权限
		return "basedata/org/jobList";
	}
	@Override
	protected String getEditView() {
		getRequest().setAttribute("workBenchTypes", WorkBenchTypeEnum.values());
		return "basedata/org/jobEdit";
	}
	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.org.dao.JobDao.select";
	}
	@Override
	protected BaseService<Job> getService() {
		return jobService;
	}
	
	@RequestMapping(value="jobLevel")
	public void jobLevel(HttpServletResponse response){
		String job = getString("job");
		Pagination<JobLevel> page = new Pagination<JobLevel>();
		if(StringUtils.isEmpty(job)){
			page.setItems(new ArrayList<JobLevel>());
		}else{
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("job", job);
			List<JobLevel> jobLevel = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobLevelDao.select", param, JobLevel.class);
			page.setItems(jobLevel);
			page.setRecordCount(jobLevel.size());
		}
		outPrint(response, JSONObject.fromObject(page, getDefaultJsonConfig()));
	}
	
	@Override
	protected boolean isAllowDelete(CoreEntity entity) {
		Job job = (Job) entity;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("job", job.getId());
		if(queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.PositionDao.select", param )>0){
			setOutputMsg("MSG", "该岗位己被引用,不能删除");
			return false;
		}
		return super.isAllowDelete(entity);
	}
	
	/**
	 * 设置微信组织
	 */
	@RequestMapping(value="setWXOrg")
	public void setWXOrg(HttpServletResponse response){
		String jobId=getString("jobId");
		String wxOrgId=getString("wxOrgId");
		if(StringUtils.isEmpty(jobId) || StringUtils.isEmpty(wxOrgId)){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "岗位为空或则微信组织为空");
		}else{
			Job job=this.jobService.getEntityById(jobId);
			WeixinOrg weixinOrg=this.weixinOrgService.getEntityById(wxOrgId);
			job.setWeixinOrg(weixinOrg);
			queryExecutor.executeUpdate("com.wuyizhiye.basedata.org.dao.JobDao.update", job);
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "设置成功");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
