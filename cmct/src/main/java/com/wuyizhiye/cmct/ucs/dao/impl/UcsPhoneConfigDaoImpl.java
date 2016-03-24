package com.wuyizhiye.cmct.ucs.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.ucs.dao.UcsPhoneConfigDao;

/**
 * @ClassName UcsPhoneConfigDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="ucsPhoneConfigDao")
public class UcsPhoneConfigDaoImpl extends BaseDaoImpl implements UcsPhoneConfigDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.ucs.dao.UcsPhoneConfigDao";
	}
}
