
package com.wuyizhiye.cmct.sms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.sms.dao.SMSConfigDao;
import com.wuyizhiye.cmct.sms.model.SMSConfig;
import com.wuyizhiye.cmct.sms.service.SMSConfigService;

/**
 * @ClassName SMSConfigServiceImpl
 * @Description 短信服务配置
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="cmctSmsConfigService")
@Transactional
public class SMSConfigServiceImpl extends BaseServiceImpl<SMSConfig> implements SMSConfigService {
	
	@Autowired
	private SMSConfigDao smsConfigDao;
	
	@Override
	protected BaseDao getDao() {
		return smsConfigDao;
	}	
}