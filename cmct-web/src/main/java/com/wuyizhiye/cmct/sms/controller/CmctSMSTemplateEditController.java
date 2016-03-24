package com.wuyizhiye.cmct.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.sms.model.SMSTemplate;
import com.wuyizhiye.cmct.sms.service.SMSTemplateService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName CmctSMSTemplateEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/smsTemplate/*")
public class CmctSMSTemplateEditController extends EditController {

	@Autowired
	private SMSTemplateService smsTemplateService;
	
	@Override
	protected Class getSubmitClass() {
		return SMSTemplate.class;
	}

	@Override
	protected BaseService getService() {
		return smsTemplateService;
	}
}
