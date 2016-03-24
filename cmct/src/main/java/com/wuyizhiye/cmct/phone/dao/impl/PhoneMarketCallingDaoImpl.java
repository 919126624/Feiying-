package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketCallingDao;

/**
 * @ClassName PhoneMarketCallingDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneMarketCallingDao")
public class PhoneMarketCallingDaoImpl extends BaseDaoImpl implements PhoneMarketCallingDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneMarketCallingDao";
	}
}
