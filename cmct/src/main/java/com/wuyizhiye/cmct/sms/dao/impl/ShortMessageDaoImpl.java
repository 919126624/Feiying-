package com.wuyizhiye.cmct.sms.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.sms.dao.ShortMessageDao;

/**
 * @ClassName ShortMessageDaoImpl
 * @Description 短信
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="cmctShortMessageDao")
public class ShortMessageDaoImpl extends BaseDaoImpl implements ShortMessageDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.sms.dao.ShortMessageDao";
	}
}
