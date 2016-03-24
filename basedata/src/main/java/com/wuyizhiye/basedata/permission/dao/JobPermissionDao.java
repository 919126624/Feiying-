package com.wuyizhiye.basedata.permission.dao;

import com.wuyizhiye.base.dao.BaseDao;

/**
 * @ClassName JobPermissionDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface JobPermissionDao extends BaseDao {
	
	void deleteByJob(String job);
	void deleteByPermissionItem(String permissionItem);
}
