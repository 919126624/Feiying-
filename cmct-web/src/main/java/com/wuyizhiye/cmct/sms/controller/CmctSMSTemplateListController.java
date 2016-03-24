package com.wuyizhiye.cmct.sms.controller;

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
import com.wuyizhiye.cmct.sms.enums.SMSTemplateEnum;
import com.wuyizhiye.cmct.sms.model.SMSTemplate;
import com.wuyizhiye.cmct.sms.service.SMSTemplateService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName CmctSMSTemplateListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/smsTemplate/*")
public class CmctSMSTemplateListController extends ListController {

	@Autowired
	private SMSTemplateService sMSTemplateService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return new SMSTemplate();
	}

	@Override
	protected String getListView() {
		return "cmct/sms/smsTemplateList";
	}

	@Override
	protected String getEditView() {
		return "cmct/sms/smsTemplateEdit";
	}

	@Override
	protected String getListMapper() {
		return SMSTemplate.MAPPER+".select";
	}

	@Override
	protected BaseService getService() {
		return sMSTemplateService;
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
		
		config.registerJsonValueProcessor(SMSTemplateEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SMSTemplateEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((SMSTemplateEnum)value).getName());
					json.put("value", ((SMSTemplateEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SMSTemplateEnum){
					return ((SMSTemplateEnum)value).getName();
				}
				return null;
			}
		});
		return config;
	}
	
	@RequestMapping(value="updateFlag")
	public void updateFlag(@RequestParam(value="id",required=true)String id,HttpServletResponse response){
		SMSTemplate tem=sMSTemplateService.getEntityById(id);
		if(tem==null){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "数据不存在");
		}else{
			if(tem.getTemplateStatus().getValue()=="ENABLED"){
				tem.setTemplateStatus(SMSTemplateEnum.DISABLE);
			}else{
				tem.setTemplateStatus(SMSTemplateEnum.ENABLED);
			}
			getService().updateEntity(tem);
			getOutputMsg().put("MSG", "操作成功！");
			getOutputMsg().put("STATE", "SUCCESS");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
}
