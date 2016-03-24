package com.wuyizhiye.cmct.sms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.sms.dao.MessageReplyDao;
import com.wuyizhiye.cmct.sms.model.MessageReply;
import com.wuyizhiye.cmct.sms.service.MessageReplyService;

/**
 * @ClassName MessageReplyServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="cmctMessageReplyService")
@Transactional
public class MessageReplyServiceImpl extends BaseServiceImpl<MessageReply> implements MessageReplyService {
	@Autowired
	private MessageReplyDao messageReplyDao;
	@Override
	protected BaseDao getDao() {
		return messageReplyDao;
	}	
}