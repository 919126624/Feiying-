package com.wuyizhiye.basedata.weChat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.weChat.dao.WeChatConfigDao;
import com.wuyizhiye.basedata.weChat.model.WeChatConfig;
import com.wuyizhiye.basedata.weChat.service.WeChatConfigService;

/**
 * @ClassName WeChatConfigServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="weChatConfigService")
@Transactional
public class WeChatConfigServiceImpl extends BaseServiceImpl<WeChatConfig> implements WeChatConfigService {
	@Autowired
	private WeChatConfigDao weChatConfigDao;
	@Override
	protected BaseDao getDao() {
		return weChatConfigDao;
	}	
}