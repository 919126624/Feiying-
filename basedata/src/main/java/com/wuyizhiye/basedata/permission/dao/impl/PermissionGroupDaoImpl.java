package com.wuyizhiye.basedata.permission.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.permission.dao.PermissionGroupDao;

/**
 * @ClassName PermissionGroupDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="permissionGroupDao")
public class PermissionGroupDaoImpl extends BaseDaoImpl implements PermissionGroupDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.permission.dao.PermissionGroupDao";
	}
}
