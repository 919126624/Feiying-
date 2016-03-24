package com.wuyizhiye.basedata.weChat.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.weChat.dao.WeChatMessageDao;

/**
 * @ClassName WeChatMessageDaoImpl
 * @Description 微信
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="weChatMessageDao")
public class WeChatMessageDaoImpl extends BaseDaoImpl implements WeChatMessageDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.weChat.dao.WeChatMessageDao";
	}
}
