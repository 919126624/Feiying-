package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneCallRecorDingDao;

/**
 * @ClassName PhoneCallRecorDingDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneCallRecorDingDao")
public class PhoneCallRecorDingDaoImpl extends BaseDaoImpl implements PhoneCallRecorDingDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneCallRecorDingDao";
	}
}
