package com.wuyizhiye.cmct.sms.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.sms.dao.SMSControlDao;

/**
 * @ClassName SMSControlDaoImpl
 * @Description 短信控制
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="cmctSmsControlDao")
public class SMSControlDaoImpl extends BaseDaoImpl implements SMSControlDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.sms.dao.SMSControlDao";
	}
}
