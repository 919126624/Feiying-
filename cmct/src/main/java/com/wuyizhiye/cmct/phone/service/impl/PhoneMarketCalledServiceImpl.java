package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketCalledDao;
import com.wuyizhiye.cmct.phone.model.PhoneMarketCalled;
import com.wuyizhiye.cmct.phone.service.PhoneMarketCalledService;

/**
 * @ClassName PhoneMarketCalledServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneMarketCalledService")
@Transactional
public class PhoneMarketCalledServiceImpl extends BaseServiceImpl<PhoneMarketCalled> implements PhoneMarketCalledService {
	@Autowired
	private PhoneMarketCalledDao phoneMarketCalledDao;
	@Override
	protected BaseDao getDao() {
		return phoneMarketCalledDao;
	}	
}