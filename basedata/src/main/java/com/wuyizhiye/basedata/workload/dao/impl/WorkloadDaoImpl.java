package com.wuyizhiye.basedata.workload.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.workload.dao.WorkloadDao;

/**
 * @ClassName WorkloadDaoImpl
 * @Description 工作量
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="workloadDao")
public class WorkloadDaoImpl extends BaseDaoImpl implements WorkloadDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.workload.dao.WorkloadDao";
	}

}
