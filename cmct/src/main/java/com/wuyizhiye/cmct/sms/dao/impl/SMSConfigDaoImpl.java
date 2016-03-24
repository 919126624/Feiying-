package com.wuyizhiye.cmct.sms.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.sms.dao.SMSConfigDao;

/**
 * @ClassName SMSConfigDaoImpl
 * @Description 短信服务配置
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="cmctSmsConfigDao")
public class SMSConfigDaoImpl extends BaseDaoImpl implements SMSConfigDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.sms.dao.SMSConfigDao";
	}
}
