package com.wuyizhiye.basedata.code.dao;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.code.model.Area;

/**
 * @ClassName AreaDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface AreaDao extends BaseDao{
	public List<Area> getListByCollection(Map<String,Object> params) ;
}
