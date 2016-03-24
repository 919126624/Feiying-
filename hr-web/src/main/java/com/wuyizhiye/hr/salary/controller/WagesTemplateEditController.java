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
import com.wuyizhiye.hr.salary.model.WagesTemplate;
import com.wuyizhiye.hr.salary.service.WagesTemplateService;


/**
 * 工资条模板controller
 * @author hyl
 *
 * @since 2014-02-12
 */
@Controller
@RequestMapping(value="hr/wagestemplate/*")
public class WagesTemplateEditController extends EditController {
	@Autowired
	private WagesTemplateService wagesTemplateService;
	@Override
	protected Class<WagesTemplate> getSubmitClass() {
		return WagesTemplate.class;
	}

	@Override
	protected BaseService<WagesTemplate> getService() {
		return this.wagesTemplateService;
	}

	@Override
	protected WagesTemplate getSubmitEntity() {
		WagesTemplate data = (WagesTemplate) super.getSubmitEntity();
		return data;
	}
	
	@Override
	protected boolean validate(Object data) {
		WagesTemplate wagesTemplate = ((WagesTemplate)data);
		if(StringUtils.isEmpty(wagesTemplate.getWageNumber())){
			getOutputMsg().put("MSG", "生成单据编号失败,请检查单据编号配置！单据编号请命名为：GZT");
			return false;
		}
		return true;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="save")
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		WagesTemplate data = getSubmitEntity();
		if(validate(data)){
			if(data instanceof WagesTemplate){
				if(StringUtils.isEmpty(((WagesTemplate)data).getId())){
					data.setCreateTime(new Date());
					data.setCreator(SystemUtil.getCurrentUser());
					data.setUpdator(SystemUtil.getCurrentUser());
					data.setLastUpdateTime(new Date());
					getService().addEntity((WagesTemplate)data);
				}else{
					data.setUpdator(SystemUtil.getCurrentUser());
					data.setLastUpdateTime(new Date());
					getService().updateEntity((WagesTemplate)data);
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
