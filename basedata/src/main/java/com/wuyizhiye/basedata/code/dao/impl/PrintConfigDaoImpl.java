package com.wuyizhiye.basedata.code.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.code.dao.PrintConfigDao;
import com.wuyizhiye.basedata.code.model.PrintConfig;

/**
 * @ClassName PrintConfigDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="printConfigDao")
public class PrintConfigDaoImpl extends BaseDaoImpl implements PrintConfigDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.code.PrintConfigDao";
	}
	@Override
	public List<PrintConfig> getList(Map<String, Object> param) {
		return this.getSqlSession().selectList(getNameSpace()+".select", param);
	}
}
