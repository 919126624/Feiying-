package com.wuyizhiye.basedata.basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.model.BasicData;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.basic.service.BasicDataTypeService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName BasicDataSupplyController
 * @Description 基础资料提供列表controller,无权限控制,可所有人访问
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/basic/basicSupply/*")
public class BasicDataSupplyController extends ListController {
	@Autowired
	private BasicDataService basicDataService;
	@Autowired
	private BasicDataTypeService basicDataTypeService;

	
	/**
	 * 设置查询xml
	 */
	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.basic.dao.BasicDataDao.select";
	}

	@Override
	protected BaseService<BasicData> getService() {
		return basicDataService;
	}

	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}
}
