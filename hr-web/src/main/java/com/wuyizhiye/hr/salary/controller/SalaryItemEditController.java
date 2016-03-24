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
import com.wuyizhiye.hr.attendance.model.Leave;
import com.wuyizhiye.hr.salary.model.SalaryItem;
import com.wuyizhiye.hr.salary.service.SalaryItemService;


/**
 * 薪酬项目controller
 * @author hyl
 *
 * @since 2014-02-12
 */
@Controller
@RequestMapping(value="hr/salaryItem/*")
public class SalaryItemEditController extends EditController {
	@Autowired
	private SalaryItemService salaryItemService;
	@Override
	protected Class<SalaryItem> getSubmitClass() {
		return SalaryItem.class;
	}

	@Override
	protected BaseService<SalaryItem> getService() {
		return this.salaryItemService;
	}

	@Override
	protected SalaryItem getSubmitEntity() {
		SalaryItem data = (SalaryItem) super.getSubmitEntity();
		return data;
	}
	
	@Override
	protected boolean validate(Object data) {
		SalaryItem salaryItem = ((SalaryItem)data);
		if(StringUtils.isEmpty(salaryItem.getNumber())){
			getOutputMsg().put("MSG", "生成单据编号失败,请检查单据编号配置！单据编号请命名为：SALARYITEM");
			return false;
		}
		return true;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="save")
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		SalaryItem data = getSubmitEntity();
		if(validate(data)){
			if(data instanceof SalaryItem){
				if(StringUtils.isEmpty(((SalaryItem)data).getId())){
					data.setCreateTime(new Date());
					data.setCreator(SystemUtil.getCurrentUser());
					data.setUpdator(SystemUtil.getCurrentUser());
					data.setLastUpdateTime(new Date());
					getService().addEntity((SalaryItem)data);
				}else{
					data.setUpdator(SystemUtil.getCurrentUser());
					data.setLastUpdateTime(new Date());
					getService().updateEntity((SalaryItem)data);
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
		String id=this.getString("id");
		String number = this.getString("number");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("neqId", id);
		param.put("fnumber", number);
		int count = queryExecutor.execCount("com.wuyizhiye.hr.salary.dao.SalaryItemDao.judgeNumber", param);
		if(count > 0){
			this.getOutputMsg().put("MSG", "该编号已经存在!");
		} else {
			this.getOutputMsg().put("MSG", null);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}	
}
