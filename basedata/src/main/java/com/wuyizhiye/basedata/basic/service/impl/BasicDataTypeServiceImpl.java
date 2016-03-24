package com.wuyizhiye.basedata.basic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.dao.BasicDataTypeDao;
import com.wuyizhiye.basedata.basic.model.BasicDataType;
import com.wuyizhiye.basedata.basic.service.BasicDataTypeService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName BasicDataTypeServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="basicDataTypeService")
@Transactional
public class BasicDataTypeServiceImpl extends DataEntityService<BasicDataType> implements
		BasicDataTypeService {
	@Autowired
	private BasicDataTypeDao basicDataTypeDao;
	@Override
	protected BaseDao getDao() {
		return basicDataTypeDao;
	}
	
	@Override
	public void addEntity(BasicDataType entity) {
		entity.setLongNumber(entity.getNumber());
		entity.setLevel(1);
		entity.setLeaf(true);
		if(entity.getParent()!=null && !StringUtils.isEmpty(entity.getParent().getId())){
			BasicDataType parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			super.updateEntity(parent);
		}
		super.addEntity(entity);
	}
	
	@Override
	public void updateEntity(BasicDataType entity) {
		entity.setLongNumber(entity.getNumber());
		entity.setLevel(1);
		entity.setLeaf(true);
		if(entity.getParent()!=null){
			BasicDataType parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			super.updateEntity(parent);
		}
		super.updateEntity(entity);
	}
}
