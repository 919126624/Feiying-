package com.wuyizhiye.cmct.wechat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.wechat.dao.WechatCommunicationDao;
import com.wuyizhiye.cmct.wechat.model.WechatCommunication;
import com.wuyizhiye.cmct.wechat.service.WechatCommunicationService;

/**
 * @ClassName WechatCommunicationServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="wechatCommunicationService")
@Transactional
public class WechatCommunicationServiceImpl extends BaseServiceImpl<WechatCommunication> implements WechatCommunicationService {
	@Autowired
	private WechatCommunicationDao wechatCommunicationDao;
	@Override
	protected BaseDao getDao() {
		return wechatCommunicationDao;
	}	
}