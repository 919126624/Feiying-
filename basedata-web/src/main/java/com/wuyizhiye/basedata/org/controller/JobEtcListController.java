package com.wuyizhiye.basedata.org.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.basic.model.BasicData;
import com.wuyizhiye.basedata.org.model.JobEtc;
import com.wuyizhiye.basedata.org.service.JobEtcService;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName JobEtcListController
 * @Description 职等
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/jobEtc/*")
public class JobEtcListController extends ListController {

	@Autowired
	private JobEtcService jobEtcService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return new JobEtc();
	}

	@Override
	protected String getListView() {
		return "basedata/org/jobEtcList";
	}

	@Override
	protected String getEditView() {
		return "basedata/org/jobEtcEdit";
	}

	@Override
	protected String getListMapper() {
		return JobEtc.MAPPER+".select";
	}

	@Override
	protected BaseService getService() {
		return jobEtcService;
	}
	
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		//日期
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
		config.registerJsonValueProcessor(UserStatusEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof UserStatusEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((UserStatusEnum)value).getName());
					json.put("value", ((UserStatusEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof UserStatusEnum){
					return ((UserStatusEnum)value).getName();
				}
				return null;
			}
		});
		return config;
	}
	
	@RequestMapping(value="enable")
	public void enable(@RequestParam(value="id",required=true)String id,@RequestParam(value="enable",required=true)String enable,HttpServletResponse response){
		JobEtc data = this.jobEtcService.getEntityById(id);
		if(data==null){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "数据不存在");
		}else{
			data.setStatus(UserStatusEnum.valueOf(enable));
			getService().updateEntity(data);
			getOutputMsg().put("STATE", "SUCCESS");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
}
