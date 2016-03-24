package com.wuyizhiye.basedata.portlet.dao;


import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.portlet.model.HomeSet;

/**
 * @ClassName HomeSetDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface HomeSetDao extends BaseDao {
	void updateStatus(HomeSet homeset);
	void updateSelective(HomeSet homeset);
	List<HomeSet> querylist(Map<String,Object> map);
}
