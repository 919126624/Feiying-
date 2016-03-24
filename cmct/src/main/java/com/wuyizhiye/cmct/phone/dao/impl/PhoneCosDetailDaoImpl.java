package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneCosDetailDao;

/**
 * @ClassName PhoneCosDetailDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneCosDetailDao")
public class PhoneCosDetailDaoImpl extends BaseDaoImpl implements PhoneCosDetailDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneCosDetailDao";
	}
}
