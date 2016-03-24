package com.wuyizhiye.basedata.info.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.info.dao.NotReadmsgDao;

/**
 * @ClassName NotReadmsgDaoImpl
 * @Description 最新消息表
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="notReadmsgDao")
public class NotReadmsgDaoImpl extends BaseDaoImpl implements NotReadmsgDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.info.dao.TimingGetNotReadInfoDao";
	}
}
