package com.wuyizhiye.basedata.weChat.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.weChat.dao.WeChatConfigDao;

/**
 * @ClassName WeChatConfigDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="weChatConfigDao")
public class WeChatConfigDaoImpl extends BaseDaoImpl implements WeChatConfigDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.weChat.dao.WeChatConfigDao";
	}
}
