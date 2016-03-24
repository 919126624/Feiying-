package com.wuyizhiye.basedata.topic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.topic.dao.DealHistoryDao;
import com.wuyizhiye.basedata.topic.model.DealHistory;
import com.wuyizhiye.basedata.topic.service.DealHistoryService;

/**
 * @ClassName DealHistoryServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="dealHistoryService")
@Transactional
public class DealHistoryServiceImpl extends BaseServiceImpl<DealHistory>
		implements DealHistoryService {
	@Autowired
	private DealHistoryDao dealHistoryDao;
	@Override
	protected BaseDao getDao() {
		return dealHistoryDao;
	}
}
