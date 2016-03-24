package com.wuyizhiye.cmct.phone.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.person.enums.UserTypeEnum;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.model.PhoneCallIn;
import com.wuyizhiye.cmct.phone.service.PhoneCallInService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneCallInListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneCallIn/*")
public class PhoneCallInListController extends ListController {

	@Autowired
	private PhoneCallInService phoneCallInService;
	
	@Override
	protected CoreEntity createNewEntity() {
		PhoneCallIn phoneCallIn=new PhoneCallIn();
		return phoneCallIn;
	}

	@Override
	protected String getListView() {
		return "cmct/phone/phoneCallInList";
	}

	@Override
	protected String getEditView() {
		return "cmct/phone/phoneCallInEdit";
	}

	@Override
	protected String getListMapper() {
		return PhoneCallIn.MAPPER+".select";
	}

	@Override
	protected BaseService getService() {
		return phoneCallInService;
	}
	
	@Override
	protected Map<String, Object> getListDataParam() {
		DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
		Map<String,Object> param=super.getListDataParam();
		if(UserTypeEnum.P01.equals(SystemUtil.getCurrentUserType())){
			param.put("personId", getCurrentUser().getId());
		}
		try{
			if(null!=param.get("startDate")){
				param.put("startDate", df.parse(param.get("startDate").toString()));
			}
			if(null!=param.get("endDate")){
				param.put("endDate", DateUtil.getNextDay(df.parse(param.get("endDate").toString())));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return param;
	}
	
	@Override
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
		return config;
	}
	
}
