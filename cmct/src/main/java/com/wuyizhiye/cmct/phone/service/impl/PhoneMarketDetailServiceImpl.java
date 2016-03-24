package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketDetailDao;
import com.wuyizhiye.cmct.phone.model.PhoneMarketDetail;
import com.wuyizhiye.cmct.phone.service.PhoneMarketDetailService;

/**
 * @ClassName PhoneMarketDetailServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneMarketDetailService")
@Transactional
public class PhoneMarketDetailServiceImpl extends BaseServiceImpl<PhoneMarketDetail> implements PhoneMarketDetailService {
	@Autowired
	private PhoneMarketDetailDao phoneMarketDetailDao;
	@Override
	protected BaseDao getDao() {
		return phoneMarketDetailDao;
	}	
}