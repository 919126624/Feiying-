package com.wuyizhiye.basedata.info.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.info.dao.InfoCenterUnreadDao;

/**
 * @ClassName InfoCenterUnreadDaoImpl
 * @Description 消息中心未读数据
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="infoCenterUnreadDao")
public class InfoCenterUnreadDaoImpl extends BaseDaoImpl implements InfoCenterUnreadDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.info.dao.InfoCenterUnreadDao";
	}
}
