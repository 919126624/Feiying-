package com.wuyizhiye.basedata.basic.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.model.BasicDataType;
import com.wuyizhiye.basedata.basic.service.BasicDataTypeService;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName BasicDataTypeListController
 * @Description 基础资料类型列表controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/basic/datatype/*")
public class BasicDataTypeListController extends TreeListController {
	@Autowired
	private BasicDataTypeService basicDataTypeService;
	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.basedata.basic.dao.BasicDataTypeDao.select";
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.basic.dao.BasicDataTypeDao.getSimpleTreeData";
	}

	@Override
	protected CoreEntity createNewEntity() {
		BasicDataType type = new BasicDataType();
		String parent = getString("parent");
		if(!StringUtils.isEmpty(parent)){
			type.setParent(getService().getEntityById(parent));
		}
		return type;
	}

	@Override
	protected String getListView() {
		return "basedata/basic/basicDataType";
	}

	@Override
	protected String getEditView() {
		return "basedata/basic/basicDataTypeEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.basic.dao.BasicDataTypeDao.select";
	}

	@Override
	protected BaseService<BasicDataType> getService() {
		return basicDataTypeService;
	}
	
	/**
	 * ${base}/basedata/basic/datatype/basicDataTypePicker?multi=true为多选
	 * @param model
	 * @return
	 */
	@RequestMapping(value="basicDataTypePicker")
	public String basicDataTypePicker(ModelMap model){
		Map<String,String> param = getParamMap();
		Set<String> keys = param.keySet();
		for(String s : keys){
			model.put(s, param.get(s));
		}
		return "basedata/basic/basicDataTypePicker";
	}
}
