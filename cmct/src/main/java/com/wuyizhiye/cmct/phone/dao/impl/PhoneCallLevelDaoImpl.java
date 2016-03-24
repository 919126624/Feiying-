package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneCallLevelDao;

/**
 * @ClassName PhoneCallLevelDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneCallLevelDao")
public class PhoneCallLevelDaoImpl extends BaseDaoImpl implements PhoneCallLevelDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneCallLevelDao";
	}
}
