/**
 * com.wuyizhiye.basedata.basic.controller.BasicDataEditController.java
 */
package com.wuyizhiye.hr.salary.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.hr.salary.model.SalaryItem;
import com.wuyizhiye.hr.salary.model.SalaryStandard;
import com.wuyizhiye.hr.salary.service.SalaryStandardService;


/**
 * 薪酬标准controller
 * @author hyl
 *
 * @since 2014-02-12
 */
@Controller
@RequestMapping(value="hr/salaryStandard/*")
public class SalaryStandardEditController extends EditController {
	@Autowired
	private SalaryStandardService salaryStandardService;
	@Override
	protected Class<SalaryStandard> getSubmitClass() {
		return SalaryStandard.class;
	}

	@Override
	protected BaseService<SalaryStandard> getService() {
		return this.salaryStandardService;
	}

	@Override
	protected SalaryStandard getSubmitEntity() {
		SalaryStandard data = (SalaryStandard) super.getSubmitEntity();
		return data;
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="save")
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		SalaryStandard data = getSubmitEntity();
		if(validate(data)){
			if(data instanceof SalaryStandard){
				if(StringUtils.isEmpty(((SalaryStandard)data).getId())){
					data.setCreateTime(new Date());
					data.setCreator(SystemUtil.getCurrentUser());
					data.setUpdator(SystemUtil.getCurrentUser());
					data.setLastUpdateTime(new Date());
					getService().addEntity((SalaryStandard)data);
				}else{
					data.setUpdator(SystemUtil.getCurrentUser());
					data.setLastUpdateTime(new Date());
					getService().updateEntity((SalaryStandard)data);
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
	 * 判断岗位是否已经存在
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="judgeJob")
	public void judgeName(HttpServletRequest request, HttpServletResponse response){
		String neId=this.getString("id");
		String jobId = this.getString("job");
		String jobLevelId = this.getString("jobLevelId");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("neId", neId);
		param.put("jobId", jobId);
		param.put("jobLevelId", jobLevelId);
		int count = queryExecutor.execCount("com.wuyizhiye.hr.salary.dao.SalaryStandardDao.judgeJob", param);
		if(count > 0){
			this.getOutputMsg().put("MSG", "该岗位标准已经存在!");
		} else {
			this.getOutputMsg().put("MSG", null);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
}
