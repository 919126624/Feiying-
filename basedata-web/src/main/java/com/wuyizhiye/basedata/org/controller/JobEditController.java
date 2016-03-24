package com.wuyizhiye.basedata.org.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.service.JobService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName JobEditController
 * @Description 岗位编辑controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/job/*")
public class JobEditController extends EditController {
	@Autowired
	private JobService jobService;
	@Override
	protected Class<Job> getSubmitClass() {
		return Job.class;
	}

	@Override
	protected BaseService<Job> getService() {
		return jobService;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Object getSubmitEntity() {
		Job job = (Job) super.getSubmitEntity();
		List<JobLevel> jlist = null;			
		String entryJson = getString("entryJson");
		if(!StringUtils.isEmpty(entryJson)){
			Collection<JobLevel> tmp = JSONArray.toCollection(JSONArray.fromObject(entryJson), JobLevel.class);
			jlist = new ArrayList<JobLevel>(tmp);
		}else{
			jlist = new ArrayList<JobLevel>();
		}
		if(job.getHasLevel()==1){	
			job.setJobLevel(jlist);
		}else{
			if(jlist.size()==0){
			JobLevel tmp = new JobLevel();
			tmp.setName(job.getName());
			tmp.setLevel(1);
			jlist.add(tmp);
			}
			job.setJobLevel(jlist);
			
		}
		return job;
	}
	
	@Override
	protected boolean validate(Object data) {
		Job entity = (Job) data;
		if(StringUtils.isEmpty(entity.getNumber())){
			getOutputMsg().put("MSG", "编码不能为空");
			return false;
		}
		if(StringUtils.isEmpty(entity.getName())){
			getOutputMsg().put("MSG", "名称不能为空");
			return false;
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("number", entity.getNumber());
		List<Job> values = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobDao.select", param, Job.class);
		for(Job g : values){
			if(!g.getId().equals(entity.getId())){
				getOutputMsg().put("MSG", "该编码己存在");
				return false;
			}
		}
		param.clear();
		param.put("name", entity.getName());
		values = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobDao.select", param, Job.class);
		for(Job g : values){
			if(!g.getId().equals(entity.getId())){
				getOutputMsg().put("MSG", "该名称己存在");
				return false;
			}
		}
		return super.validate(data);
	}
}
