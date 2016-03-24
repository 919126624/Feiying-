package com.wuyizhiye.basedata.portlet.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.portlet.dao.QuickSetDao;

/**
 * @ClassName QuickSetDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="quickSetDao")
public class QuickSetDaoImpl extends BaseDaoImpl implements QuickSetDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.portlet.dao.QuickSetDao";
	}
}
