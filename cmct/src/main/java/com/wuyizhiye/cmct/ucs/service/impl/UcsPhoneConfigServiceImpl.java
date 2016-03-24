package com.wuyizhiye.cmct.ucs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.ucs.dao.UcsPhoneConfigDao;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneConfig;
import com.wuyizhiye.cmct.ucs.service.UcsPhoneConfigService;

/**
 * @ClassName UcsPhoneConfigServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="ucsPhoneConfigService")
@Transactional
public class UcsPhoneConfigServiceImpl extends BaseServiceImpl<UcsPhoneConfig> implements UcsPhoneConfigService {
	@Autowired
	private UcsPhoneConfigDao ucsPhoneConfigDao;
	@Override
	protected BaseDao getDao() {
		return ucsPhoneConfigDao;
	}	
}