package com.wuyizhiye.cmct.wechat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.wechat.dao.WechatAmountDao;
import com.wuyizhiye.cmct.wechat.model.WechatAmount;
import com.wuyizhiye.cmct.wechat.service.WechatAmountService;

/**
 * @ClassName WechatAmountServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="wechatAmountService")
@Transactional
public class WechatAmountServiceImpl extends BaseServiceImpl<WechatAmount> implements WechatAmountService {
	@Autowired
	private WechatAmountDao wechatAmountDao;
	@Override
	protected BaseDao getDao() {
		return wechatAmountDao;
	}	
}