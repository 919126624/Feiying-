package com.wuyizhiye.basedata.portlet.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.portlet.dao.QuickMenuItemDao;

/**
 * @ClassName QuickMenuItemDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="quickMenuItemDao")
public class QuickMenuItemDaoImpl extends BaseDaoImpl implements QuickMenuItemDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.portlet.dao.QuickMenuItemDao";
	}
}
