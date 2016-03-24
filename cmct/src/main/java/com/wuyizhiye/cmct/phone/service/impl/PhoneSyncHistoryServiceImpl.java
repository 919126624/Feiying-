package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneSyncHistoryDao;
import com.wuyizhiye.cmct.phone.model.PhoneSyncHistory;
import com.wuyizhiye.cmct.phone.service.PhoneSyncHistoryService;

/**
 * @ClassName PhoneSyncHistoryServiceImpl
 * @Description 呼叫中心-同步话单
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneSyncHistoryService")
@Transactional
public class PhoneSyncHistoryServiceImpl extends BaseServiceImpl<PhoneSyncHistory> implements PhoneSyncHistoryService {
	
	@Autowired
	private PhoneSyncHistoryDao phoneSyncHistoryDao;
	
	@Override
	protected BaseDao getDao() {
		return phoneSyncHistoryDao;
	}	
}