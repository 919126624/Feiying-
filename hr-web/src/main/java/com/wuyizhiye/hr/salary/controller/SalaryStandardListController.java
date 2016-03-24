package com.wuyizhiye.hr.salary.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.hr.enums.SalaryItemTypeEnum;
import com.wuyizhiye.hr.salary.model.SalaryItem;
import com.wuyizhiye.hr.salary.model.SalaryStandard;
import com.wuyizhiye.hr.salary.model.StandardItem;
import com.wuyizhiye.hr.salary.service.SalaryStandardService;

/**
 * 薪酬标准controller
 * @author hyl
 *
 * @since 2014-02-12
 */
@Controller
@RequestMapping(value="hr/salaryStandard/*")
public class SalaryStandardListController extends ListController{
	@Autowired
	private SalaryStandardService salaryStandardService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return new SalaryStandard();
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "hr/salary/salarysetting/salaryStandardList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return "hr/salary/salarysetting/salaryStandardEdit";
	}	
	/**
	 * json转换config
	 * @return
	 */
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
		return config;
	}
	@RequestMapping(value="jobLevel")
	public void jobLevel(HttpServletResponse response){
		String job = getString("job");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("job", job);
		List<JobLevel> jobLevel = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobLevelDao.select", param, JobLevel.class);
		outPrint(response, JSONArray.fromObject(jobLevel, getDefaultJsonConfig()));
	}
	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.hr.salary.dao.SalaryStandardDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return salaryStandardService;
	}
}
