package com.wuyizhiye.basedata.portlet.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.portlet.dao.QuickJobItemDao;

/**
 * @ClassName QuickJobItemDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="quickJobItemDao")
public class QuickJobItemDaoImpl extends BaseDaoImpl implements QuickJobItemDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.portlet.dao.QuickJobItemDao";
	}
}
