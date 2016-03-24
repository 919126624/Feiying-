package com.wuyizhiye.basedata.basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.basic.model.BasicDataType;
import com.wuyizhiye.basedata.basic.service.BasicDataTypeService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName BasicDataTypeEditController
 * @Description 基础资料类型编辑controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/basic/datatype/*")
public class BasicDataTypeEditController extends EditController {
	@Autowired
	private BasicDataTypeService basicDataTypeService;
	@Override
	protected Class<BasicDataType> getSubmitClass() {
		return BasicDataType.class;
	}

	@Override
	protected BaseService<BasicDataType> getService() {
		return basicDataTypeService;
	}
}
