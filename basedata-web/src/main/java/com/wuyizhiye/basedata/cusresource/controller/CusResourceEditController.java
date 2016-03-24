package com.wuyizhiye.basedata.cusresource.controller;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.cusresource.model.CusResource;
import com.wuyizhiye.basedata.cusresource.service.CusResourceService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName CusResourceEditController
 * @Description 基础资料编辑controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/cusresource/*")
public class CusResourceEditController extends EditController {
	@Autowired
	private CusResourceService cusResourceService;
	@Override
	protected Class<CusResource> getSubmitClass() {
		return CusResource.class;
	}

	@Override
	protected BaseService<CusResource> getService() {
		return cusResourceService;
	}

	@Override
	protected Object getSubmitEntity() {
		CusResource data = (CusResource) super.getSubmitEntity();
		data.setEnable(true);
		return data;
	}
	
	@RequestMapping(value="enable")
	public void enable(@RequestParam(value="id",required=true)String id,@RequestParam(value="enable",required=true)boolean enable,HttpServletResponse response){
		CusResource data = getService().getEntityById(id);
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
}
