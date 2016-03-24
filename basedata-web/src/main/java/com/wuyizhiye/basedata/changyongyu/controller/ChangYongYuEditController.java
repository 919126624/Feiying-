package com.wuyizhiye.basedata.changyongyu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.basic.model.ChangYongYu;
import com.wuyizhiye.basedata.basic.service.ChangYongYuService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName ChangYongYuEditController
 * @Description 自定义常用语编辑controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/changyongyu/*")
public class ChangYongYuEditController extends EditController {
	
	@Autowired
	private ChangYongYuService changYongYuService;
	
	@Override
	protected Class<ChangYongYu> getSubmitClass() {
		return ChangYongYu.class;
	}

	@Override
	protected BaseService<ChangYongYu> getService() {
		return changYongYuService;
	}
}
