package com.wuyizhiye.basedata.monitor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.basedata.monitor.model.TomcatMonitor;
import com.wuyizhiye.basedata.monitor.service.TomcatMonitorService;
import com.wuyizhiye.basedata.redis.dao.RedisDao;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName MonitorListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping("basedata/monitor/*")
public class MonitorListController extends ListController{
	private static Logger logger=Logger.getLogger(MonitorListController.class);
	@Autowired
	private TomcatMonitorService tomcatMonitorService ;

	@Override
	protected CoreEntity createNewEntity() {
		return new TomcatMonitor();
	}

	@Override
	protected String getListView() {
		return "basedata/monitor/monitorList";
	}

	@Override
	protected String getEditView() {
		return "basedata/monitor/monitorEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.monitor.dao.TomcatMonitorDao.select";
	}

	@Override
	protected BaseService getService() {
		return tomcatMonitorService;
	}
	
	@RequestMapping(value="getData")
	@Dependence(method="list")
	public void getData(Pagination<TomcatMonitor> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery(getListMapper(), pagination, getListDataParam());
		if(pagination!=null && pagination.getItems().size() > 0){
			RedisDao redisdao = ApplicationContextAware.getApplicationContext().getBean(RedisDao.class);
			for(TomcatMonitor item : pagination.getItems()){
				tomcatMonitorService.getMonitorInfo(item);
				item.setRunRedis(redisdao.useRedis());
				//查找应用用户数
				String url = getPath(item.getPort()) + "/api/framework/online?dataCenter="+DataSourceHolder.getDataSource();
				try{
					String result = HttpClientUtil.callHttpUrl(url, "");
					JSONObject json = JSONObject.fromObject(result);
					item.setPcOnline(json.getString("pc"));
					item.setMobileOnline(json.getString("mobile"));
					item.setTotalUser(json.getString("online") == null ? 0 : Integer.valueOf(json.getString("online")));
				}catch (Exception e) {
					logger.error("", e);
					item.setPcOnline("0");
					item.setMobileOnline("0");
					item.setTotalUser(0);
				}
			}
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	@RequestMapping("isReName")
	public void isReName(String id,String newName,HttpServletResponse response){
		//判断数据库中是否存在该名字
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("currentRcoderId", id);
		param.put("existName", newName);
		int count = queryExecutor.execCount(TomcatMonitor.MAPPER+".isExistName", param);
		if(count > 0){
			getOutputMsg().put("STATE", "FAIl");
			getOutputMsg().put("MSG", "应用已经维护！");
		}else{
			getOutputMsg().put("STATE", "SUCCESS");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	private String getPath(int port){
		port = port == 0  ? getRequest().getServerPort() : port ;
		String scheme = getRequest().getScheme();
		String path = getRequest().getScheme()+"://"+getRequest().getServerName() + ((("http".equals(scheme) && port == 80) ||("https".equals(scheme)  && port == 443)) ? "" : ":" + port) + getRequest().getContextPath();
		return path ;
	}
	
	
}
