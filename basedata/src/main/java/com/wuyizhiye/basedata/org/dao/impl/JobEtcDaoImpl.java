package com.wuyizhiye.basedata.org.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.JobEtcDao;

/**
 * @ClassName JobEtcDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="jobEtcDao")
public class JobEtcDaoImpl extends BaseDaoImpl implements JobEtcDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.org.dao.JobEtcDao";
	}
}
