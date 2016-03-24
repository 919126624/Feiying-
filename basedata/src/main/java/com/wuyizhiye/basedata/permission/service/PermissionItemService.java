package com.wuyizhiye.basedata.permission.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.permission.model.PermissionItem;

/**
 * @ClassName PermissionItemService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PermissionItemService extends BaseService<PermissionItem> {
	void loadPermissions();
	
	List<PermissionItem> getPermissionItemList(Map<String,Object> map);
}
