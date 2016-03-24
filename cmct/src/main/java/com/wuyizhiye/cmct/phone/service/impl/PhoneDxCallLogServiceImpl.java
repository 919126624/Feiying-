package com.wuyizhiye.cmct.phone.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneDxCallLogDao;
import com.wuyizhiye.cmct.phone.model.PhoneDxCallLog;
import com.wuyizhiye.cmct.phone.service.PhoneDxCallLogService;

/**
 * @ClassName PhoneDxCallLogServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneDxCallLogService")
@Transactional
public class PhoneDxCallLogServiceImpl extends BaseServiceImpl<PhoneDxCallLog> implements PhoneDxCallLogService {
	@Autowired
	private PhoneDxCallLogDao phoneDxCallLogDao;
	@Override
	protected BaseDao getDao() {
		return phoneDxCallLogDao;
	}
	@Override
	public void addEntity(PhoneDxCallLog entity) {
		entity.setCurrentDate(new Date());
		super.addEntity(entity);
	}	
}