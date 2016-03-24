package com.wuyizhiye.basedata.monitor.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.monitor.dao.TomcatMonitorDao;

/**
 * @ClassName TomcatMonitorDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="tomcatMonitorDao")
public class TomcatMonitorDaoImpl extends BaseDaoImpl implements TomcatMonitorDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.monitor.dao.TomcatMonitorDao";
	}
}
