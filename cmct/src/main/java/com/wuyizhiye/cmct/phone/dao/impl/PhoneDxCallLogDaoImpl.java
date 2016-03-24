package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneDxCallLogDao;

/**
 * @ClassName PhoneDxCallLogDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneDxCallLogDao")
public class PhoneDxCallLogDaoImpl extends BaseDaoImpl implements PhoneDxCallLogDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneDxCallLogDao";
	}
}
