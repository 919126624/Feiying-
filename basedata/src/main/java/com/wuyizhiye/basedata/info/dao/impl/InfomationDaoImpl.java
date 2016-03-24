package com.wuyizhiye.basedata.info.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.info.dao.InfomationDao;

/**
 * @ClassName InfomationDaoImpl
 * @Description 消息提醒
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="infomationDao")
public class InfomationDaoImpl extends BaseDaoImpl implements InfomationDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.info.dao.InfomationDao";
	}
}
