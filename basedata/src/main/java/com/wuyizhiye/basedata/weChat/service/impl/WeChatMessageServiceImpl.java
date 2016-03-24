package com.wuyizhiye.basedata.weChat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.weChat.dao.WeChatMessageDao;
import com.wuyizhiye.basedata.weChat.model.WeChatMessage;
import com.wuyizhiye.basedata.weChat.service.WeChatMessageService;

/**
 * @ClassName WeChatMessageServiceImpl
 * @Description 微信消息
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value = "weChatMessageService")
@Transactional
public class WeChatMessageServiceImpl extends DataEntityService<WeChatMessage>
		implements WeChatMessageService {

	@Autowired
	private WeChatMessageDao weChatMessageDao;

	@Override
	protected BaseDao getDao() {
		return weChatMessageDao;
	}
   

}
