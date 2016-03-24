package com.wuyizhiye.basedata.apiCenter.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.apiCenter.model.APIVisitLog;
import com.wuyizhiye.basedata.apiCenter.service.APIVisitLogService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName APIVisitLogListController
 * @Description 接口调用日志controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/apivisitlog/*")
public class APIVisitLogListController extends ListController {
	
	@Autowired
	private APIVisitLogService aPIVisitLogService;
	
	@Override
	protected CoreEntity createNewEntity() {
		 return null;
	}
	
	@Override
	protected String getListView() {
		List<Map> tables = queryExecutor.execQuery("com.wuyizhiye.basedata.apiCenter.dao.APIVisitLogDao.selectTables", null, Map.class);
		StringBuilder builder = new StringBuilder();
		for(Map map : tables){
			builder.append(map.get("TABLE_NAME").toString().replace("T_BD_APIVISITLOG", "")).append(",");
		}
		getRequest().setAttribute("tables", builder.toString());
		return "basedata/apiCenter/apiVisitLogList";
	}
	 
	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.apiCenter.dao.APIVisitLogDao.select";	
	}

	@Override
	protected BaseService<APIVisitLog> getService() {
		return aPIVisitLogService;
	}
 
	@Override
	protected String getEditView() {
		return "";
	}
	
	/**
	 * 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listData")
	@Dependence(method="list")
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery(getListMapper(), pagination, getListDataParam());
		afterFetchListData(pagination);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	@Override
	protected JsonConfig getDefaultJsonConfig() {
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if (value != null && value instanceof Date) {
					return sdf.format(value);
				}
				return null;
			}

			public Object processArrayValue(Object value, JsonConfig cfg) {
				if (value != null && value instanceof Date) {
					return sdf.format(value);
				}
				return null;
			}
		});
		return config;
	}
	  
}
