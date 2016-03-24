package com.wuyizhiye.basedata.cusresource.controller;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.cusresource.model.CusResource;
import com.wuyizhiye.basedata.cusresource.service.CusResourceService;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName CusResourceListController
 * @Description 基础资料列表controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/cusresource/*")
public class CusResourceListController extends TreeListController {
	@Autowired
	private CusResourceService cusResourceService;

	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.basedata.cusresource.dao.CusResourceDao.select";
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.cusresource.dao.CusResourceDao.getSimpleTreeData";
	}

	@Override
	protected CoreEntity createNewEntity() {
		CusResource type = new CusResource();
		String parent = getString("parent");
		if(!StringUtils.isEmpty(parent)){
			type.setParent(getService().getEntityById(parent));
		}
		return type;
	}

	@Override
	protected String getListView() {
		return "basedata/basic/cusResourceList";
	}

	@Override
	protected String getEditView() {
		return "basedata/basic/cusResourceEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.cusresource.dao.CusResourceDao.select";
	}

	@Override
	protected BaseService<CusResource> getService() {
		return cusResourceService;
	}
	
	/**
	 * @param model
	 * @return
	 */
	@RequestMapping(value="cusResourcePicker")
	public String cusResourcePicker(ModelMap model){
		Map<String,String> param = getParamMap();
		Set<String> keys = param.keySet();
		for(String s : keys){
			model.put(s, param.get(s));
		}
		return "basedata/basic/cusResourcePicker";
	}
	
	
	@RequestMapping(value="pickParentData")
	public void pickParentData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery(getSimpleTreeDataMapper(), pagination, getListDataParam());
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
}
