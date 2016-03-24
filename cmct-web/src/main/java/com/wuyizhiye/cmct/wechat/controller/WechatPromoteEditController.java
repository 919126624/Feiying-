package com.wuyizhiye.cmct.wechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.wechat.model.WechatPromote;
import com.wuyizhiye.cmct.wechat.service.WechatPromoteService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName WechatPromoteEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/wechatPromote/*")
public class WechatPromoteEditController extends EditController {

	@Autowired
	private WechatPromoteService wechatPromoteService;
	
	@Override
	protected Class getSubmitClass() {
		return WechatPromote.class;
	}

	@Override
	protected BaseService getService() {
		return wechatPromoteService;
	}
}
