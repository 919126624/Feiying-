package com.wuyizhiye.basedata.org.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.BusinessType;
import com.wuyizhiye.basedata.org.service.BusinessTypeService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName BusinessTypeEditController
 * @Description 业务类型编辑controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/businessType/*")
public class BusinessTypeEditController extends EditController {
	@Autowired
	private BusinessTypeService businessTypeService;
	@Override
	protected Class<BusinessType> getSubmitClass() {
		return BusinessType.class;
	}

	@Override
	protected BaseService<BusinessType> getService() {
		return businessTypeService;
	}
	
	@Override
	protected Object getSubmitEntity() {
		BusinessType data = (BusinessType) super.getSubmitEntity();
		if(StringUtils.isEmpty(data.getId())){
			data.setEnable(true);
		}
		return data;
	}
	
	@RequestMapping(value="enable")
	public void enable(@RequestParam(value="id",required=true)String id,@RequestParam(value="enable",required=true)boolean enable,HttpServletResponse response){
		BusinessType data = getService().getEntityById(id);
		if(data==null){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "数据不存在");
		}else{
			data.setEnable(enable);
			getService().updateEntity(data);
			getOutputMsg().put("STATE", "SUCCESS");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@Override
	protected boolean validate(Object data) {
		BusinessType btype = (BusinessType) data;
		Map<String, Object> param = new HashMap<String, Object>();
		param.clear();
		param.put("number", btype.getNumber());
		List<BusinessType> types = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.BusinessTypeDao.select", param, BusinessType.class);
		if(types!=null && types.size()>0){
			for(BusinessType t : types){
				if(t.getNumber().equals(btype.getNumber()) && !t.getId().equals(btype.getId())){
					getOutputMsg().put("MSG", "该编码己存在");
					return false;
				}
			}
		}
		param.clear();
		param.put("name", btype.getName());
		types = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.BusinessTypeDao.select", param, BusinessType.class);
		if(types!=null && types.size()>0){
			for(BusinessType t : types){
				if(t.getName().equals(btype.getName()) && !t.getId().equals(btype.getId())){
					getOutputMsg().put("MSG", "该名称己存在");
					return false;
				}
			}
		}
		return super.validate(data);
	}
}
