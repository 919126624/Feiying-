package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneDeputyNumDao;

/**
 * @ClassName PhoneDeputyNumDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneDeputyNumDao")
public class PhoneDeputyNumDaoImpl extends BaseDaoImpl implements PhoneDeputyNumDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneDeputyNumDao";
	}
}
