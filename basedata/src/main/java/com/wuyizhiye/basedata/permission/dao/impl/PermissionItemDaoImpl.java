package com.wuyizhiye.basedata.permission.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.permission.dao.PermissionItemDao;
import com.wuyizhiye.basedata.permission.model.PermissionItem;

/**
 * @ClassName PermissionItemDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="permissionItemDao")
public class PermissionItemDaoImpl extends BaseDaoImpl implements PermissionItemDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.permission.dao.PermissionItemDao";
	}

	@Override
	public List<PermissionItem> getPermissionItemList(Map<String, Object> map) {
		return this.getSqlSession().selectList(getNameSpace()+".selectNoJoin", map);
	}

}
