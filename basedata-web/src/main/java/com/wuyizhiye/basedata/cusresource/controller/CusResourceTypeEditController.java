package com.wuyizhiye.basedata.cusresource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.cusresource.model.CusResourceType;
import com.wuyizhiye.basedata.cusresource.service.CusResourceTypeService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName CusResourceTypeEditController
 * @Description 基础资料类型编辑controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/cusresourceType/*")
public class CusResourceTypeEditController extends EditController {
	@Autowired
	private CusResourceTypeService cusResourceTypeService;
	@Override
	protected Class<CusResourceType> getSubmitClass() {
		return CusResourceType.class;
	}

	@Override
	protected BaseService<CusResourceType> getService() {
		return cusResourceTypeService;
	}
}
