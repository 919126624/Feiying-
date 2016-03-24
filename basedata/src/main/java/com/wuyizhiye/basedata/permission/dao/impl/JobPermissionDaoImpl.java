package com.wuyizhiye.basedata.permission.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.permission.dao.JobPermissionDao;

/**
 * @ClassName JobPermissionDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="jobPermissionDao")
public class JobPermissionDaoImpl extends BaseDaoImpl implements
		JobPermissionDao {

	@Override
	public void deleteByJob(String job) {
		getSqlSession().delete(getNameSpace() + ".deleteByJob", job);
	}

	@Override
	public void deleteByPermissionItem(String permissionItem) {
		getSqlSession().delete(getNameSpace() + ".deleteByPermissionItem", permissionItem);
	}

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.permission.dao.JobPermissionDao";
	}

}
