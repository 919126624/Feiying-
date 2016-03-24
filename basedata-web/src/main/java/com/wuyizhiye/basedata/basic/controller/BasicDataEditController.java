package com.wuyizhiye.basedata.basic.controller;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.basic.model.BasicData;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName BasicDataEditController
 * @Description 基础资料编辑controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/basic/basicdata/*")
public class BasicDataEditController extends EditController {
	@Autowired
	private BasicDataService basicDataService;
	@Override
	protected Class<BasicData> getSubmitClass() {
		return BasicData.class;
	}

	@Override
	protected BaseService<BasicData> getService() {
		return basicDataService;
	}

	@Override
	protected Object getSubmitEntity() {
		BasicData data = (BasicData) super.getSubmitEntity();
		data.setEnable(true);
		return data;
	}
	
	@RequestMapping(value="enable")
	public void enable(@RequestParam(value="id",required=true)String id,@RequestParam(value="enable",required=true)boolean enable,HttpServletResponse response){
		BasicData data = getService().getEntityById(id);
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
