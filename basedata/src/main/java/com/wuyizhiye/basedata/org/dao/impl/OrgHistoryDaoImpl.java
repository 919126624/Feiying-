package com.wuyizhiye.basedata.org.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.OrgHistoryDao;

/**
 * @ClassName OrgHistoryDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="orgHistoryDao")
public class OrgHistoryDaoImpl extends BaseDaoImpl implements OrgHistoryDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.org.dao.OrgHistoryDao";
	}
}
