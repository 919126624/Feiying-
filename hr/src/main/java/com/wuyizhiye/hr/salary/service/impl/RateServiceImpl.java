/**
 * com.wuyizhiye.hr.salary.service.impl.RateServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.salary.dao.RateDao;
import com.wuyizhiye.hr.salary.model.Rate;
import com.wuyizhiye.hr.salary.service.RateService;

/**
 * 组织service
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="rateService")
@Transactional
public class RateServiceImpl extends BaseServiceImpl<Rate> implements RateService {
	@Autowired
	private RateDao rateDao;
	@Override
	protected BaseDao getDao() {
		return rateDao;
	}	
}
