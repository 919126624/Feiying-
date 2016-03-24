package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneSyncHistoryDao;

/**
 * @ClassName PhoneSyncHistoryDaoImpl
 * @Description 呼叫中心-同步话单
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneSyncHistoryDao")
public class PhoneSyncHistoryDaoImpl extends BaseDaoImpl implements PhoneSyncHistoryDao {
	
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneSyncHistoryDao";
	}
}
