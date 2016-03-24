package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneDxCallErrorStatusDao;
import com.wuyizhiye.cmct.phone.model.PhoneDxCallErrorStatus;
import com.wuyizhiye.cmct.phone.service.PhoneDxCallErrorStatusService;

/**
 * @ClassName PhoneDxCallErrorStatusServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneDxCallErrorStatusService")
@Transactional
public class PhoneDxCallErrorStatusServiceImpl extends BaseServiceImpl<PhoneDxCallErrorStatus> implements PhoneDxCallErrorStatusService {
	@Autowired
	private PhoneDxCallErrorStatusDao phoneDxCallErrorStatusDao;
	@Override
	protected BaseDao getDao() {
		return phoneDxCallErrorStatusDao;
	}	
}