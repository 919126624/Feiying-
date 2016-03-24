package com.wuyizhiye.basedata.monitor.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.monitor.model.TomcatMonitor;

/**
 * @ClassName TomcatMonitorService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface TomcatMonitorService extends BaseService<TomcatMonitor> {
	
	/**
	 * 获取实例服务器的监控信息
	 * @param tomcat
	 */
	public void getMonitorInfo(TomcatMonitor tomcat);
	
}
