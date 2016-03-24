package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketCalledDao;

/**
 * @ClassName PhoneMarketCalledDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneMarketCalledDao")
public class PhoneMarketCalledDaoImpl extends BaseDaoImpl implements PhoneMarketCalledDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneMarketCalledDao";
	}
}
