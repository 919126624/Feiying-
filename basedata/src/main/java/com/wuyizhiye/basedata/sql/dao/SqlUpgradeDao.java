package com.wuyizhiye.basedata.sql.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.sql.model.SqlUpgrade;

/**
 * @ClassName SqlUpgradeDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface SqlUpgradeDao extends BaseDao {
	
	List<SqlUpgrade> getSqlUpgradeList(Map<String,Object> map);
	
	void updateRun(SqlUpgrade s);
	
	void updateSqlBatch(List<SqlUpgrade> list);

}
