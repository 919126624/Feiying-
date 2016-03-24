package com.wuyizhiye.basedata.cusresource.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.cusresource.dao.CusResourceDao;
import com.wuyizhiye.basedata.cusresource.model.CusResource;
import com.wuyizhiye.basedata.cusresource.service.CusResourceService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName CusResourceServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="cusResourceService")
@Transactional
public class CusResourceServiceImpl extends DataEntityService<CusResource>
		implements CusResourceService {
	@Autowired
	private CusResourceDao cusResourceDao;
	@Override
	protected BaseDao getDao() {
		return cusResourceDao;
	}
	@Override
	public List<CusResource> getCusResourceList(Map<String, Object> param) {
		return cusResourceDao.getCusResourceList(param);
	}
	@Override
	public CusResource getEntityByNumber(String number) {
		return this.cusResourceDao.getEntityByNumber(number);
	}

}
