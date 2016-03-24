package com.wuyizhiye.basedata.permission.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.basedata.permission.dao.PermissionGroupDao;
import com.wuyizhiye.basedata.permission.model.PermissionGroup;
import com.wuyizhiye.basedata.permission.service.PermissionGroupService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName PermissionGroupServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="permissionGroupService")
@Transactional
public class PermissionGroupServiceImpl extends
DataEntityService<PermissionGroup> implements
		PermissionGroupService {
	@Autowired
	private PermissionGroupDao permissionGroupDao;
	@Autowired
	private QueryExecutor queryExecutor;
	@Override
	protected BaseDao getDao() {
		return permissionGroupDao;
	}
	
	@Override
	public void addEntity(PermissionGroup entity) {
		entity.setLongNumber(entity.getNumber());
		entity.setLevel(1);
		entity.setLeaf(true);
		if(entity.getParent()!=null){
			PermissionGroup parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			super.updateEntity(parent);
		}
		super.addEntity(entity);
	}
	
	@Override
	public void updateEntity(PermissionGroup entity) {
		entity.setLongNumber(entity.getNumber());
		entity.setLevel(1);
		entity.setLeaf(true);
		if(entity.getParent()!=null){
			PermissionGroup parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			super.updateEntity(parent);
		}
		super.updateEntity(entity);
	}
	
	@Override
	public void deleteEntity(PermissionGroup entity) {
		super.deleteEntity(entity);
		if(entity.getParent()!=null){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("parent", entity.getParent().getId());
			if(queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.PermissionGroupDao.select", new Pagination<PermissionGroup>(), param ).getItems().size()==0){
				PermissionGroup parent = permissionGroupDao.getEntityById(entity.getParent().getId());
				parent.setLeaf(true);
				super.updateEntity(parent);
			}
		}
	}
}
