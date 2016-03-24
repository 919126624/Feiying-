package com.wuyizhiye.basedata.cchat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.cchat.dao.NewestCChatDao;
import com.wuyizhiye.basedata.cchat.model.NewestCChat;
import com.wuyizhiye.basedata.cchat.service.NewestCChatService;

/**
 * @ClassName NewestCChatServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="basedatacommonOrderService")
@Transactional
public class NewestCChatServiceImpl extends BaseServiceImpl<NewestCChat> implements NewestCChatService {
	@Autowired
	private NewestCChatDao newestCChatDao;
	@Override
	protected BaseDao getDao() {
		return newestCChatDao;
	}	
}