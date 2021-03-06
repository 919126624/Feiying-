package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneDxCallErrorStatusDao;

/**
 * @ClassName PhoneDxCallErrorStatusDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneDxCallErrorStatusDao")
public class PhoneDxCallErrorStatusDaoImpl extends BaseDaoImpl implements PhoneDxCallErrorStatusDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneDxCallErrorStatusDao";
	}
}
