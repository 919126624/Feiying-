package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneCodeDao;
import com.wuyizhiye.cmct.phone.model.PhoneCode;
import com.wuyizhiye.cmct.phone.service.PhoneCodeService;

/**
 * @ClassName PhoneCodeServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneCodeService")
@Transactional
public class PhoneCodeServiceImpl extends BaseServiceImpl<PhoneCode> implements PhoneCodeService {
	@Autowired
	private PhoneCodeDao phoneCodeDao;
	@Override
	protected BaseDao getDao() {
		return phoneCodeDao;
	}	
}