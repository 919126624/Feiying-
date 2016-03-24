package com.wuyizhiye.basedata.permission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.model.PermissionItem;
import com.wuyizhiye.basedata.permission.service.PermissionItemService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PermissionItemEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="permission/permissionItem/*")
public class PermissionItemEditController extends EditController {
	@Autowired
	private PermissionItemService permissionService;
	@Override
	protected Class<PermissionItem> getSubmitClass() {
		return PermissionItem.class;
	}

	@Override
	protected BaseService<PermissionItem> getService() {
		return permissionService;
	}
	
	@Override
	protected boolean validate(Object data) {
		PermissionItem entity = (PermissionItem) data;
		if(StringUtils.isEmpty(entity.getName())){
			getOutputMsg().put("MSG", "名称不能为空");
			return false;
		}
		if(StringUtils.isEmpty(entity.getNumber())){
			getOutputMsg().put("MSG", "编码不能为空");
			return false;
		}
 		return super.validate(data);
	}
}
