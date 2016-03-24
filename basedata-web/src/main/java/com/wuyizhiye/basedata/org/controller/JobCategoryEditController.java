package com.wuyizhiye.basedata.org.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.JobCategory;
import com.wuyizhiye.basedata.org.service.JobCategoryService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName JobCategoryEditController
 * @Description 岗位大类编辑controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/jobcategory/*")
public class JobCategoryEditController extends EditController {
	@Autowired
	private JobCategoryService jobCategoryService;
	@Autowired
	private QueryExecutor queryExecutor;
	@Override
	protected Class<JobCategory> getSubmitClass() {
		return JobCategory.class;
	}

	@Override
	protected BaseService<JobCategory> getService() {
		return this.jobCategoryService;
	}

	@Override
	protected JobCategory getSubmitEntity() {
		JobCategory data = (JobCategory) super.getSubmitEntity();
		//data.setUpdator(SystemUtil.getCurrentUser());
		return data;
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="save")
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		JobCategory data = getSubmitEntity();
		if(validate(data)){
			if(data instanceof JobCategory){
				if(StringUtils.isEmpty(((JobCategory)data).getId())){
					data.setCreateTime(new Date());
					data.setCreator(SystemUtil.getCurrentUser());
					data.setUpdator(SystemUtil.getCurrentUser());
					data.setLastUpdateTime(new Date());
					getService().addEntity((JobCategory)data);
				}else{
					data.setUpdator(SystemUtil.getCurrentUser());
					data.setLastUpdateTime(new Date());
					getService().updateEntity((JobCategory)data);
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
	/**
	 * 判断编码是否已经存在
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="judgeNumber")
	public void judgeNumber(HttpServletRequest request, HttpServletResponse response){
		String neqId=this.getString("id");
		String number = this.getString("number");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("neqId", neqId);
		param.put("fnumber", number);
		int count = queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.JobCategoryDao.judgeNumber", param);
		if(count > 0){
			this.getOutputMsg().put("MSG", "该编号已经存在!");
		} else {
			this.getOutputMsg().put("MSG", null);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	@Override
	protected boolean validate(Object data) {
		JobCategory jobCategory = (JobCategory)data;
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("name", jobCategory.getName());
		int count = queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.JobCategoryDao.judgeName", param);
		if(count > 0&&getString("id")==null){
			getOutputMsg().put("MSG", "该名称己存在");
			return false;
		}
		return super.validate(data);
	}
	
}
