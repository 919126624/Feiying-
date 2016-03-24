package com.wuyizhiye.basedata.permission.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.permission.model.Menu;

/**
 * @ClassName MenuDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface MenuDao extends BaseDao {
	List<Menu> getMenuList(Map<String,Object> map);
}
