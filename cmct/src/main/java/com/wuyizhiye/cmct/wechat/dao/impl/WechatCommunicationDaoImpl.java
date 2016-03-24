package com.wuyizhiye.cmct.wechat.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.wechat.dao.WechatCommunicationDao;

/**
 * @ClassName WechatCommunicationDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="wechatCommunicationDao")
public class WechatCommunicationDaoImpl extends BaseDaoImpl implements WechatCommunicationDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.wechat.dao.WechatCommunicationDao";
	}
}
