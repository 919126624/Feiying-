/**
 * com.wuyizhiye.basedata.basic.controller.BasicDataEditController.java
 */
package com.wuyizhiye.hr.salary.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import com.wuyizhiye.hr.salary.model.Rate;
import com.wuyizhiye.hr.salary.service.RateService;


/**
 * 税率controller
 * @author hyl
 *
 * @since 2014-02-12
 */
@Controller
@RequestMapping(value="hr/rate/*")
public class RateEditController extends EditController {
	@Autowired
	private RateService reateService;
	@Override
	protected Class<Rate> getSubmitClass() {
		return Rate.class;
	}

	@Override
	protected BaseService<Rate> getService() {
		return this.reateService;
	}

	@Override
	protected Rate getSubmitEntity() {
		Rate data = (Rate) super.getSubmitEntity();
		return data;
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="save")
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Rate data = getSubmitEntity();
		if(validate(data)){
			if(data instanceof Rate){
				if(StringUtils.isEmpty(((Rate)data).getId())){
					data.setCreateTime(new Date());
					data.setCreator(SystemUtil.getCurrentUser());
					data.setUpdator(SystemUtil.getCurrentUser());
					data.setLastUpdateTime(new Date());
					getService().addEntity((Rate)data);
				}else{
					data.setUpdator(SystemUtil.getCurrentUser());
					data.setLastUpdateTime(new Date());
					getService().updateEntity((Rate)data);
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
		
}
