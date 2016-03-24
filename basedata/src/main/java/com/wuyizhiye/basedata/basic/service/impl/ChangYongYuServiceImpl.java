package com.wuyizhiye.basedata.basic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.basic.dao.ChangYongYuDao;
import com.wuyizhiye.basedata.basic.model.ChangYongYu;
import com.wuyizhiye.basedata.basic.service.ChangYongYuService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName ChangYongYuServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="changYongYuService")
@Transactional
public class ChangYongYuServiceImpl extends DataEntityService<ChangYongYu> implements
		ChangYongYuService {
	
	@Autowired
	private ChangYongYuDao changYongYuDao;
	
	@Override
	protected BaseDao getDao() {
		return changYongYuDao;
	}
	
	
}
