package com.wuyizhiye.basedata.cusresource.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.cusresource.model.CusResourceType;
import com.wuyizhiye.basedata.cusresource.service.CusResourceTypeService;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName CusResourceTypeListController
 * @Description 基础资料类型列表controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/cusresourceType/*")
public class CusResourceTypeListController extends TreeListController {
	@Autowired
	private CusResourceTypeService cusResourceTypeService;
	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.basedata.cusresource.dao.CusResourceTypeDao.select";
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.cusresource.dao.CusResourceTypeDao.getSimpleTreeData";
	}

	@Override
	protected CoreEntity createNewEntity() {
		CusResourceType type = new CusResourceType();
		String parent = getString("parent");
		if(!StringUtils.isEmpty(parent)){
			type.setParent(getService().getEntityById(parent));
		}
		return type;
	}

	@Override
	protected String getListView() {
		return "basedata/basic/cusResourceType";
	}

	@Override
	protected String getEditView() {
		return "basedata/basic/cusResourceTypeEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.cusresource.dao.CusResourceTypeDao.select";
	}

	@Override
	protected BaseService<CusResourceType> getService() {
		return cusResourceTypeService;
	}
	
	/**
	 * @param model
	 * @return
	 */
	@RequestMapping(value="cusResourceTypePicker")
	public String cusResourceTypePicker(ModelMap model){
		Map<String,String> param = getParamMap();
		Set<String> keys = param.keySet();
		for(String s : keys){
			model.put(s, param.get(s));
		}
		return "basedata/basic/cusResourceTypePicker";
	}
}
