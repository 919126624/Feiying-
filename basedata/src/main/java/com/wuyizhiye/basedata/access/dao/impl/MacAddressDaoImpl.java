package com.wuyizhiye.basedata.access.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.access.dao.MacAddressDao;

/**
 * @ClassName MacAddressDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="macAddressDao")
public class MacAddressDaoImpl extends BaseDaoImpl implements MacAddressDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.access.dao.MacAddressDao";
	}
}
