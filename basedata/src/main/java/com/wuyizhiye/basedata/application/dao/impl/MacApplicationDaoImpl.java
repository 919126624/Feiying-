package com.wuyizhiye.basedata.application.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.application.dao.MacApplicationDao;

/**
 * @ClassName MacApplicationDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="macApplicationDao")
public class MacApplicationDaoImpl extends BaseDaoImpl implements
		MacApplicationDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.application.dao.MacApplicationDao";
	}

}
