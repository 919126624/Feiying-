package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketBindPersonDao;

/**
 * @ClassName PhoneMarketBindPersonDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneMarketBindPersonDao")
public class PhoneMarketBindPersonDaoImpl extends BaseDaoImpl implements PhoneMarketBindPersonDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneMarketBindPersonDao";
	}
}
