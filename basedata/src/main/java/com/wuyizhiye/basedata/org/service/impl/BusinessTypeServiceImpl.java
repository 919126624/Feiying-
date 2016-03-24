package com.wuyizhiye.basedata.org.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.org.dao.BusinessTypeDao;
import com.wuyizhiye.basedata.org.model.BusinessType;
import com.wuyizhiye.basedata.org.service.BusinessTypeService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName BusinessTypeServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="businessTypeService")
@Transactional
public class BusinessTypeServiceImpl extends DataEntityService<BusinessType>
		implements BusinessTypeService {
	@Autowired
	private BusinessTypeDao businessTypeDao;
	@Override
	protected BaseDao getDao() {
		return businessTypeDao;
	}
	
	@Override
	@CacheEvict(value="allBusinessTypes")
	public void addEntity(BusinessType entity) {
		super.addEntity(entity);
	}

	@Override
	@CacheEvict(value="allBusinessTypes")
	public void updateEntity(BusinessType entity) {
		super.updateEntity(entity);
	}

	@Override
	@CacheEvict(value="allBusinessTypes")
	public void deleteEntity(BusinessType entity) {
		super.deleteEntity(entity);
	}

	@Override
	@CacheEvict(value="allBusinessTypes")
	public void deleteById(String id) {
		super.deleteById(id);
	}

	@Override
	public List<BusinessType> getAllBusinessTypes() {
		return queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.BusinessTypeDao.select", new HashMap<String,Object>(), BusinessType.class);
	}

	@Override
	public List<BusinessType> getAllBusinessTypes(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.BusinessTypeDao.select", map, BusinessType.class);
	}
}
