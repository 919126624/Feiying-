package com.wuyizhiye.basedata.sql.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.sql.dao.SqlUpgradeDao;
import com.wuyizhiye.basedata.sql.model.SqlUpgrade;

/**
 * @ClassName SqlUpgradeDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="sqlUpgradeDao")
public class SqlUpgradeDaoImpl extends BaseDaoImpl implements SqlUpgradeDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.sql.dao.SqlUpgradeDao";
	}

	@Override
	public void updateRun(SqlUpgrade s) {
		this.getSqlSession().update(getNameSpace()+".updateRun", s);
	}

	@Override
	public List<SqlUpgrade> getSqlUpgradeList(Map<String, Object> map) {
		
		return this.getSqlSession().selectList(getNameSpace()+".select", map);
	}

	@Override
	public void updateSqlBatch(List<SqlUpgrade> list) {
		for(int i=0;i<list.size();i++){
			this.getSqlSession().update(getNameSpace()+".updateSql", list.get(i));
		}
		
	}
}
