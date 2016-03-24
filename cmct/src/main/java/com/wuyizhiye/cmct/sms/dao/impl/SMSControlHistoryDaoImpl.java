package com.wuyizhiye.cmct.sms.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.sms.dao.SMSControlHistoryDao;

/**
 * @ClassName SMSControlHistoryDaoImpl
 * @Description 短信控制  策略历史
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="cmctSmsControlHistoryDao")
public class SMSControlHistoryDaoImpl extends BaseDaoImpl implements SMSControlHistoryDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.sms.dao.SMSControlHistoryDao";
	}
}
