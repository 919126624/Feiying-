package com.wuyizhiye.basedata.permission.dao;

import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;

/**
 * @ClassName ShortcutsDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface ShortcutsDao extends BaseDao {
	int getMaxIndex(Map<String,Object> param);
}
