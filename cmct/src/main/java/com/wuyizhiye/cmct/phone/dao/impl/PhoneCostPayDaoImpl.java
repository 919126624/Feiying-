package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneCostPayDao;

/**
 * @ClassName PhoneCostPayDaoImpl
 * @Description 话费充值dao接口实现
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneCostPayDao")
public class PhoneCostPayDaoImpl extends BaseDaoImpl implements PhoneCostPayDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneCostPayDao";
	}

}
