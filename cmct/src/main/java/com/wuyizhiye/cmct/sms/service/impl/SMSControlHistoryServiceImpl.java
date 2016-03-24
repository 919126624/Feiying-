
package com.wuyizhiye.cmct.sms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.sms.dao.SMSControlHistoryDao;
import com.wuyizhiye.cmct.sms.model.SMSControlHistory;
import com.wuyizhiye.cmct.sms.service.SMSControlHistoryService;

/**
 * @ClassName SMSControlHistoryServiceImpl
 * @Description 短信控制 历史
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="cmctSmsControlHistoryService")
@Transactional
public class SMSControlHistoryServiceImpl extends BaseServiceImpl<SMSControlHistory> implements SMSControlHistoryService {
	
	@Autowired
	private SMSControlHistoryDao smsControlHistoryDao;
	
	@Override
	protected BaseDao getDao() {
		return smsControlHistoryDao;
	}	
}