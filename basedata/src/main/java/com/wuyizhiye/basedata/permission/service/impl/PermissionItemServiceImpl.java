package com.wuyizhiye.basedata.permission.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.basedata.permission.dao.PermissionItemDao;
import com.wuyizhiye.basedata.permission.model.PermissionItem;
import com.wuyizhiye.basedata.permission.service.PermissionItemService;

/**
 * @ClassName PermissionItemServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="permissionItemService")
@Transactional
public class PermissionItemServiceImpl extends BaseServiceImpl<PermissionItem>
		implements PermissionItemService {
	@Autowired
	private PermissionItemDao permissionDao;
	
	@Override
	protected BaseDao getDao() {
		return permissionDao;
	}

	@Override
	public synchronized void loadPermissions() {
		List<String> methods = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.PermissionItemDao.selectMethodList", new HashMap<String,Object>(), String.class);
		Set<String> permissionMethods = new HashSet<String>();
		for(String method : methods){
			permissionMethods.add(method);
		}
		 ContextLoader.getCurrentWebApplicationContext()
			.getServletContext().setAttribute("WHOLE-PERMISSIONMETHODS-"+DataSourceHolder.getDataSource(), permissionMethods);
	
	}

	@Override
	public List<PermissionItem> getPermissionItemList(Map<String, Object> map) {
		
		return this.permissionDao.getPermissionItemList(map);
		
	}
}
