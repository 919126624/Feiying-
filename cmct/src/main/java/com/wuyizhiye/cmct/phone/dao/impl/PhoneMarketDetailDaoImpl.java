package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketDetailDao;

/**
 * @ClassName PhoneMarketDetailDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneMarketDetailDao")
public class PhoneMarketDetailDaoImpl extends BaseDaoImpl implements PhoneMarketDetailDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneMarketDetailDao";
	}
}
