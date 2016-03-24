package com.wuyizhiye.basedata.permission.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.model.PermissionGroup;
import com.wuyizhiye.basedata.permission.service.PermissionGroupService;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName PermissionGroupListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="permission/permissionGroup/*")
public class PermissionGroupListController extends TreeListController {
	@Autowired
	private PermissionGroupService permissionGroupService;
	
	@Override
	protected CoreEntity createNewEntity() {
		String parentId = getString("parent");
		PermissionGroup entity = new PermissionGroup();
		if(!StringUtils.isEmpty(parentId)){
			entity.setParent(permissionGroupService.getEntityById(parentId));
		}
		return entity;
	}
	@Override
	protected String getListView() {
		return null;
	}

	@Override
	protected String getEditView() {
		return "permission/permissionGroupEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.permission.dao.PermissionGroupDao.select";
	}
	@Override
	protected BaseService<PermissionGroup> getService() {
		return permissionGroupService;
	}
	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.basedata.permission.dao.PermissionGroupDao.getChild";
	}
	
	/**
	 * ${base}/basedata/org/orgDataPicker?multi=true为多选
	 * @param model
	 * @return
	 */
	@RequestMapping(value="permissionGroupDataPicker")
	public String permissionGroupDataPicker(ModelMap model){
		Map<String,String> param = getParamMap();
		Set<String> keys = param.keySet();
		for(String s : keys){
			model.put(s, param.get(s));
		}
		return "permission/permissionGroupDataPicker";
	}
	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.permission.dao.PermissionGroupDao.getSimpleTreeData";
	}
	
	@Override
	protected boolean isAllowDelete(CoreEntity entity) {
		PermissionGroup group = (PermissionGroup) entity;
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("parent", group.getId());
		if(queryExecutor.execCount(getTreeDataMapper(), param)>0){
			setOutputMsg("STATE", "FAIL");
			setOutputMsg("MSG", "存在下级分组,不能删除");
			return false;
		}
		param.clear();
		param.put("group", group.getId());
		if(queryExecutor.execCount("com.wuyizhiye.basedata.permission.dao.PermissionItemDao.select", param)>0){
			setOutputMsg("STATE", "FAIL");
			setOutputMsg("MSG", "该分组下存在权限项,不能删除");
			return false;
		}
		return super.isAllowDelete(entity);
	}

}
