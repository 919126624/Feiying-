package com.wuyizhiye.basedata.permission.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.permission.model.PermissionItem;

/**
 * @ClassName PermissionItemDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PermissionItemDao extends BaseDao {
	List<PermissionItem> getPermissionItemList(Map<String,Object> map);
}
