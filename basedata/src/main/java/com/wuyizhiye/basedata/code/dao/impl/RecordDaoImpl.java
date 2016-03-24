package com.wuyizhiye.basedata.code.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.code.dao.RecordDao;

/**
 * @ClassName RecordDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="recordDao")
public class RecordDaoImpl extends BaseDaoImpl implements RecordDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.code.RecordDao";
	}

	@Override 
	public void deleteByRule(String id) {
		getSqlSession().delete(getNameSpace() + ".deleteByRule", id);
	}
}
