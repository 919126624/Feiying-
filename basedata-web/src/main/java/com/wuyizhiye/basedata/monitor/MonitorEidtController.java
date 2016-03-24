package com.wuyizhiye.basedata.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.monitor.model.TomcatMonitor;
import com.wuyizhiye.basedata.monitor.service.TomcatMonitorService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName MonitorEidtController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping("basedata/monitor/*")
public class MonitorEidtController extends EditController {

	@Autowired
	private TomcatMonitorService tomcatMonitorService ;
	
	@Override
	protected Class getSubmitClass() {
		return TomcatMonitor.class;
	}

	@Override
	protected BaseService getService() {
		return tomcatMonitorService;
	}

}
