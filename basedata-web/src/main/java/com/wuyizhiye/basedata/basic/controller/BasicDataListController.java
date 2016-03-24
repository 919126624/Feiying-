package com.wuyizhiye.basedata.basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.model.BasicData;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.basic.service.BasicDataTypeService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName BasicDataListController
 * @Description 基础资料列表controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/basic/basicdata/*")
public class BasicDataListController extends ListController {
	@Autowired
	private BasicDataService basicDataService;
	@Autowired
	private BasicDataTypeService basicDataTypeService;

	@Override
	protected CoreEntity createNewEntity() {
		BasicData data = new BasicData();
		String type = getString("type");
		if(!StringUtils.isEmpty(type)){
			data.setType(basicDataTypeService.getEntityById(type));
		}
		return data;
	}

	@Override
	protected String getListView() {
		return "basedata/basic/basicDataList";
	}
	// 基础资料合并
	@RequestMapping(value="basicDataView")
	protected String getListView2(ModelMap  model) {	
		return "basedata/basic/basicDataView";
	}
	@Override
	protected String getEditView() {
		return "basedata/basic/basicDataEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.basic.dao.BasicDataDao.select";
	}

	@Override
	protected BaseService<BasicData> getService() {
		return basicDataService;
	}
}
