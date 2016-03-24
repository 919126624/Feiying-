package com.wuyizhiye.cmct.wechat.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.wechat.dao.WechatAmountDao;

/**
 * @ClassName WechatAmountDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="wechatAmountDao")
public class WechatAmountDaoImpl extends BaseDaoImpl implements WechatAmountDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.wechat.dao.WechatAmountDao";
	}
}
