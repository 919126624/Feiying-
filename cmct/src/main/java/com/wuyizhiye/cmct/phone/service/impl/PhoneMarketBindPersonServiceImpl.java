package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketBindPersonDao;
import com.wuyizhiye.cmct.phone.model.PhoneMarketBindPerson;
import com.wuyizhiye.cmct.phone.service.PhoneMarketBindPersonService;

/**
 * @ClassName PhoneMarketBindPersonServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneMarketBindPersonService")
@Transactional
public class PhoneMarketBindPersonServiceImpl extends BaseServiceImpl<PhoneMarketBindPerson> implements PhoneMarketBindPersonService {
	@Autowired
	private PhoneMarketBindPersonDao phoneMarketBindPersonDao;
	@Override
	protected BaseDao getDao() {
		return phoneMarketBindPersonDao;
	}	
}