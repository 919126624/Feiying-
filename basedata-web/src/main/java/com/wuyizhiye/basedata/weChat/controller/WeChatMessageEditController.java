package com.wuyizhiye.basedata.weChat.controller;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.topic.model.DealHistory;
import com.wuyizhiye.basedata.weChat.model.WeChatMessage;
import com.wuyizhiye.basedata.weChat.service.WeChatMessageService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName WeChatMessageEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/weChat/*")//new
public class WeChatMessageEditController extends EditController {
	@Autowired
	private WeChatMessageService weChatMessageService;
	@Override
	protected Class<WeChatMessage> getSubmitClass() {
		return WeChatMessage.class;
	}

	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		DealHistory data = (DealHistory) getSubmitEntity();
		if(validate(data)){ 
			
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@Override
	protected BaseService<WeChatMessage> getService() {
		return weChatMessageService;
	}

}
