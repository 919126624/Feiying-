package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketCallingDao;
import com.wuyizhiye.cmct.phone.model.PhoneMarketCalling;
import com.wuyizhiye.cmct.phone.service.PhoneMarketCallingService;

/**
 * @ClassName PhoneMarketCallingServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneMarketCallingService")
@Transactional
public class PhoneMarketCallingServiceImpl extends BaseServiceImpl<PhoneMarketCalling> implements PhoneMarketCallingService {
	@Autowired
	private PhoneMarketCallingDao phoneMarketCallingDao;
	@Override
	protected BaseDao getDao() {
		return phoneMarketCallingDao;
	}	
}