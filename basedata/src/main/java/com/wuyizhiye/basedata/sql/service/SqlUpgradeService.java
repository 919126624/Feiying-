package com.wuyizhiye.basedata.sql.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.sql.model.SqlUpgrade;

/**
 * @ClassName SqlUpgradeService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface SqlUpgradeService extends BaseService<SqlUpgrade> {
	List<SqlUpgrade> getSqlUpgradeList(Map<String,Object> map);
	
	void updateRun(SqlUpgrade s);
	
	void updateSqlBatch(List<SqlUpgrade> list);
}
