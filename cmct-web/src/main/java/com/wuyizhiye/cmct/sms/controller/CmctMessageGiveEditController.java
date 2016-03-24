package com.wuyizhiye.cmct.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.sms.model.MessageGive;
import com.wuyizhiye.cmct.sms.service.MessageGiveService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName CmctMessageGiveEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/messageGive/*")
public class CmctMessageGiveEditController extends EditController{
	
	@Autowired
	private MessageGiveService messageGiveService;

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getSubmitClass() {
		// TODO Auto-generated method stub
		return MessageGive.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return messageGiveService;
	}

}
