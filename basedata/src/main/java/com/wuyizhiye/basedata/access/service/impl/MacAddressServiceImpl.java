package com.wuyizhiye.basedata.access.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.access.dao.MacAddressDao;
import com.wuyizhiye.basedata.access.model.MacAddress;
import com.wuyizhiye.basedata.access.service.MacAddressService;

/**
 * @ClassName MacAddressServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="macAddressService")
@Transactional
public class MacAddressServiceImpl extends BaseServiceImpl<MacAddress> implements MacAddressService {
		@Autowired
		private MacAddressDao macAddressDao;
		@Override
		protected BaseDao getDao() {
			return macAddressDao;
	}	
}