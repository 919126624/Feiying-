package com.wuyizhiye.basedata.org.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.JobDao;

/**
 * @ClassName JobDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="jobDao")
public class JobDaoImpl extends BaseDaoImpl implements JobDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.org.dao.JobDao";
	}
}
