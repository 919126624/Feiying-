package com.wuyizhiye.cmct.wechat.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.wechat.dao.WechatPromoteDao;
import com.wuyizhiye.cmct.wechat.model.WechatPromote;
import com.wuyizhiye.cmct.wechat.service.WechatPromoteService;

/**
 * @ClassName WechatPromoteServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="wechatPromoteService")
@Transactional
public class WechatPromoteServiceImpl extends DataEntityService<WechatPromote> implements WechatPromoteService {
	@Autowired
	private WechatPromoteDao wechatPromoteDao;
	@Override
	protected BaseDao getDao() {
		return wechatPromoteDao;
	}
	@Override
	public void addEntity(WechatPromote entity) {
		entity.setCreateTime(new Date());
		entity.setCreator(SystemUtil.getCurrentUser());
		entity.setControlUnit(SystemUtil.getCurrentControlUnit());
		entity.setOrg(SystemUtil.getCurrentOrg());
		wechatPromoteDao.addEntity(entity);
	}
}