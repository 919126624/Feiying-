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
import com.wuyizhiye.framework.util.BeanUtils;
import com.wuyizhiye.hr.salary.model.Duration;
import com.wuyizhiye.hr.salary.service.DurationService;


/**
 * 薪酬期间controller
 * @author hyl
 *
 * @since 2014-02-12
 */
@Controller
@RequestMapping(value="hr/duration/*")
public class DurationEditController extends EditController {
	@Autowired
	private DurationService durationService;
	@Override
	protected Class<Duration> getSubmitClass() {
		return Duration.class;
	}

	@Override
	protected BaseService<Duration> getService() {
		return this.durationService;
	}

	@Override
	protected Duration getSubmitEntity() {
		Duration data = (Duration) super.getSubmitEntity();
		return data;
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="save")
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Duration data = getSubmitEntity();
		if(validate(data)){
			if(data instanceof Duration){
				if(StringUtils.isEmpty(((Duration)data).getId())){
					data.setCreateTime(new Date());
					data.setCreator(SystemUtil.getCurrentUser());
					data.setUpdator(SystemUtil.getCurrentUser());
					data.setLastUpdateTime(new Date());
					getService().addEntity((Duration)data);
				}else{
					data.setUpdator(SystemUtil.getCurrentUser());
					data.setLastUpdateTime(new Date());
					getService().updateEntity((Duration)data);
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
	 * 判断期间是否已经存在
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="judgeDate")
	public void judgeDate(HttpServletRequest request, HttpServletResponse response){
		String neId=this.getString("id");
		String startDate = this.getString("startDate");
		String endDate = this.getString("endDate");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("neId", neId);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		int count = queryExecutor.execCount("com.wuyizhiye.hr.salary.dao.DurationDao.judgeDate", param);
		if(count > 0){
			this.getOutputMsg().put("MSG", "该期间已经存在!");
		} else {
			this.getOutputMsg().put("MSG", null);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
}
