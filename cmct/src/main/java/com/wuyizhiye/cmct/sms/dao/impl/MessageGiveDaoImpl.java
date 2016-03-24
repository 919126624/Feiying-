package com.wuyizhiye.cmct.sms.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.sms.dao.MessageGiveDao;

/**
 * @ClassName MessageGiveDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="cmctMessageGiveDao")
public class MessageGiveDaoImpl extends BaseDaoImpl implements MessageGiveDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.sms.dao.MessageGiveDao";
	}
}
