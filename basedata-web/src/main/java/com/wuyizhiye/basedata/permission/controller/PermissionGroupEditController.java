package com.wuyizhiye.basedata.permission.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.model.PermissionGroup;
import com.wuyizhiye.basedata.permission.service.PermissionGroupService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PermissionGroupEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="permission/permissionGroup/*")
public class PermissionGroupEditController extends EditController {
	@Autowired
	private PermissionGroupService permissionGroupService;
	@Override
	protected Class<PermissionGroup> getSubmitClass() {
		return PermissionGroup.class;
	}

	@Override
	protected BaseService<PermissionGroup> getService() {
		return permissionGroupService;
	}
	
	@Override
	protected boolean validate(Object data) {
		PermissionGroup entity = (PermissionGroup) data;
		if(StringUtils.isEmpty(entity.getNumber())){
			getOutputMsg().put("MSG", "编码不能为空");
			return false;
		}
		if(StringUtils.isEmpty(entity.getName())){
			getOutputMsg().put("MSG", "名称不能为空");
			return false;
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("number", entity.getNumber());
		List<PermissionGroup> values = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.PermissionGroupDao.select", param, PermissionGroup.class);
		for(PermissionGroup g : values){
			if(!g.getId().equals(entity.getId())){
				getOutputMsg().put("MSG", "该编码己存在");
				return false;
			}
		}
		param.clear();
		param.put("name", entity.getName());
		values = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.PermissionGroupDao.select", param, PermissionGroup.class);
		for(PermissionGroup g : values){
			if(!g.getId().equals(entity.getId())){
				getOutputMsg().put("MSG", "该名称己存在");
				return false;
			}
		}
 		return super.validate(data);
	}
}
