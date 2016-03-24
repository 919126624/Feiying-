package com.wuyizhiye.basedata.cusresource.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.cusresource.dao.CusResourceTypeDao;
import com.wuyizhiye.basedata.cusresource.model.CusResourceType;
import com.wuyizhiye.basedata.cusresource.service.CusResourceTypeService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName CusResourceTypeServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="cusResourceTypeService")
@Transactional
public class CusResourceTypeServiceImpl extends DataEntityService<CusResourceType> implements
CusResourceTypeService {
	@Autowired
	private CusResourceTypeDao cusResourceTypeDao;
	@Override
	protected BaseDao getDao() {
		return cusResourceTypeDao;
	}
	
	@Override
	public void addEntity(CusResourceType entity) {
		entity.setLongNumber(entity.getNumber());
		entity.setLevel(1);
		entity.setLeaf(true);
		if(entity.getParent()!=null && !StringUtils.isEmpty(entity.getParent().getId())){
			CusResourceType parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			super.updateEntity(parent);
		}
		super.addEntity(entity);
	}
	
	@Override
	public void updateEntity(CusResourceType entity) {
		entity.setLongNumber(entity.getNumber());
		entity.setLevel(1);
		entity.setLeaf(true);
		if(entity.getParent()!=null){
			CusResourceType parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			super.updateEntity(parent);
		}
		super.updateEntity(entity);
	}
}
