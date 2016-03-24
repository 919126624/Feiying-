package com.wuyizhiye.basedata.topic.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.topic.dao.DealHistoryDao;

/**
 * @ClassName DealHistoryDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="dealHistoryDao")
public class DealHistoryDaoImpl extends BaseDaoImpl implements DealHistoryDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.topic.dao.DealHistoryDao";
	}

}
