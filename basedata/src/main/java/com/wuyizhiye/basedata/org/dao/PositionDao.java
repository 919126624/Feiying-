package com.wuyizhiye.basedata.org.dao;

import java.util.List;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.org.model.Position;

/**
 * @ClassName PositionDao
 * @Description 职位Dao
 * @author li.biao
 * @date 2015-4-2
 */
public interface PositionDao extends BaseDao {
	List<Position> getByOrg(String org);
}
