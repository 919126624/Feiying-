package com.wuyizhiye.cmct.sms.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.sms.dao.MessageReplyDao;

/**
 * @ClassName MessageReplyDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="cmctMessageReplyDao")
public class MessageReplyDaoImpl extends BaseDaoImpl implements MessageReplyDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.sms.dao.MessageReplyDao";
	}
}
