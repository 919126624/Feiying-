package com.wuyizhiye.basedata.sql.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.sql.dao.SqlUpgradeDao;
import com.wuyizhiye.basedata.sql.model.SqlUpgrade;
import com.wuyizhiye.basedata.sql.service.SqlUpgradeService;

/**
 * @ClassName SqlUpgradeServiceImpl
 * @Description 组织service
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="sqlUpgradeService")
@Transactional
public class SqlUpgradeServiceImpl extends BaseServiceImpl<SqlUpgrade> implements SqlUpgradeService {
	@Autowired
	private SqlUpgradeDao sqlUpgradeDao;
	@Override
	protected BaseDao getDao() {
		return sqlUpgradeDao;
	}
	@Override
	public List<SqlUpgrade> getSqlUpgradeList(Map<String, Object> map) {
		
		return sqlUpgradeDao.getSqlUpgradeList(map);
	}
	@Override
	public void updateRun(SqlUpgrade s) {
		this.sqlUpgradeDao.updateRun(s);
		
	}
	@Override
	public void updateSqlBatch(List<SqlUpgrade> list) {
		this.sqlUpgradeDao.updateSqlBatch(list);
		
	}	
}