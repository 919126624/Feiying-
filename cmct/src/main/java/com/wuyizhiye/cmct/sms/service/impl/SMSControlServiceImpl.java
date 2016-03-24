
package com.wuyizhiye.cmct.sms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.sms.dao.SMSControlDao;
import com.wuyizhiye.cmct.sms.model.SMSControl;
import com.wuyizhiye.cmct.sms.service.SMSControlService;

/**
 * @ClassName SMSControlServiceImpl
 * @Description 短信控制
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="cmctSmsControlService")
@Transactional
public class SMSControlServiceImpl extends BaseServiceImpl<SMSControl> implements SMSControlService {
	
	@Autowired
	private SMSControlDao smsControlDao;
	
	@Override
	protected BaseDao getDao() {
		return smsControlDao;
	}	
}