package com.wuyizhiye.basedata.permission.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;

/**
 * @ClassName PersonPermissionDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PersonPermissionDao extends BaseDao {
	void deleteByPersonAndPosition(String person,String position);
	/**
	 * 获取己存在权限
	 * @param param
	 * @return
	 */
	List<String> getExistsPermission(Map<String,Object> param);
}
