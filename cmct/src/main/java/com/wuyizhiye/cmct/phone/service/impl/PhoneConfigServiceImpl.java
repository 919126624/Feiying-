package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.cmct.phone.dao.PhoneConfigDao;
import com.wuyizhiye.cmct.phone.model.PhoneConfig;
import com.wuyizhiye.cmct.phone.service.PhoneConfigService;

/**
 * @ClassName PhoneConfigServiceImpl
 * @Description 呼叫中心-话伴参数配置  逻辑处理中心实现
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneConfigService")
@Transactional
public class PhoneConfigServiceImpl extends DataEntityService<PhoneConfig> implements PhoneConfigService {
	
	@Autowired
	private PhoneConfigDao phoneConfigDao;
	
	@Override
	protected BaseDao getDao() {
		return phoneConfigDao;
	}	
}