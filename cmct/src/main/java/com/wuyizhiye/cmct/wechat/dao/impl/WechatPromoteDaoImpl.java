package com.wuyizhiye.cmct.wechat.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.wechat.dao.WechatPromoteDao;

/**
 * @ClassName WechatPromoteDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="wechatPromoteDao")
public class WechatPromoteDaoImpl extends BaseDaoImpl implements WechatPromoteDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.wechat.dao.WechatPromoteDao";
	}
}
