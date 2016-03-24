package com.wuyizhiye.cmct.wechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.wechat.model.WechatCommunication;
import com.wuyizhiye.cmct.wechat.service.WechatCommunicationService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName WechatCommunicationEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/wechatCommunication/*")
public class WechatCommunicationEditController extends EditController {

	@Autowired
	private WechatCommunicationService wechatCommunicationService;
	
	@Override
	protected Class getSubmitClass() {
		return WechatCommunication.class;
	}

	@Override
	protected BaseService getService() {
		return wechatCommunicationService;
	}
}
