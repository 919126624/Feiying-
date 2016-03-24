
package com.wuyizhiye.basedata.operationlog.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.monitor.MonitorListController;
import com.wuyizhiye.basedata.operationlog.model.OperationLog;
import com.wuyizhiye.basedata.util.CommonExcelExportUtils;
import com.wuyizhiye.basedata.util.OperationLogUtil;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName OperationLogController
 * @Description 操作日志
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/operationlog/*")
public class OperationLogController extends BaseController {
	private static Logger logger=Logger.getLogger(OperationLogController.class);
	/**
	 * 记录操作日志
	 */
	@RequestMapping(value="saveOperationLog")
	public void saveOperationLog(HttpServletResponse response){
		String url = getString("url");
		String description = getString("description");
		 
		try {
			OperationLogUtil.saveOperationLog(url, description, null, null);
			getOutputMsg().put("STATE", "SUCCESS");
		} catch (Exception e) {
			getOutputMsg().put("STATE", "FAIL");
			logger.error("", e);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig())); 
	}
	
	/**
	 * 查询参数
	 * @return
	 * @throws ParseException 
	 */
	protected Map<String,Object> getListDataParam(){
		Map<String,String> param = getParamMap();
		Map<String,Object> params = new HashMap<String, Object>();
		Set<String> keys = param.keySet();
		String thisDateStr = "2014-11-01";//此月开始分表，如果时间少于次月查询老表
		Date theDate = DateUtil.convertStrToDate(thisDateStr,"yyyy-MM");
		for(String key : keys){
			if(key.equals("queryMonth")){
				String dateStr = param.get(key);
				Date date = DateUtil.convertStrToDate(dateStr,"yyyy-MM");
				String queryStartDate = dateStr+"-01";
				Date lDate = DateUtil.getLastDay(date);
				String queryEndDate = DateUtil.convertDateToStr(lDate);
				params.put("queryStartDate", queryStartDate);
				params.put("queryEndDate", queryEndDate);
				if(DateUtil.compareDate(date,theDate)>=0){//查询时间大于初始时间则查询分表
					if(DateUtil.compareDate(date,new Date())<0){//查询时间超出当前时间查询最近的表
						params.put("suffix", OperationLogUtil.getSuffix(date));
					}else{
						params.put("suffix", OperationLogUtil.getSuffix(new Date()));
					}
				}else{
					params.put("suffix", "");
				}
			}else{
				if(key.equals("queryDay")){
					String dateStr = param.get(key);
					Date date = DateUtil.convertStrToDate(dateStr,"yyyy-MM");
					
					params.put("queryStartDate", dateStr);
					params.put("queryEndDate", dateStr);
					
					if(DateUtil.compareDate(date,theDate)>=0){//查询时间大于初始时间则查询分表
						if(DateUtil.compareDate(date,new Date())<0){//查询时间超出当前时间查询最近的表
							params.put("suffix", OperationLogUtil.getSuffix(date));
						}else{
							params.put("suffix", OperationLogUtil.getSuffix(new Date()));
						}
					}else{
						params.put("suffix", "");
					}
				}else{
					params.put(key, param.get(key));
				}
			}
		}
		
		return params;
	}
	
	@RequestMapping(value="list")
	public String list(){
		getRequest().setAttribute("currentMonth", DateUtil.convertDateToStr(new Date(),"yyyy-MM"));
		getRequest().setAttribute("currentDay", DateUtil.convertDateToStr(new Date()));
		return "basedata/operationlog/operationLogList";
	}
	
	/**
	 * json转换config
	 * @return
	 */
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		return config;
	}
	
	/**
	 * 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listData")
	@Dependence(method="list")
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.orepationlog.dao.OperationLogDao.getByCond", pagination, getListDataParam());
		 
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	
	/**
	 * 查询月考勤页面  导出Excel
	 * @param response
	 */
	@RequestMapping(value="exportData")
	public void exportData(HttpServletResponse response)throws IOException {
		 
		response.setContentType("application/octet-stream");
		String fileName = "异常信息.xlsx";
        response.addHeader("content-disposition", "attachment; filename=" +URLEncoder.encode(fileName, "utf-8") + "");
        OutputStream os = response.getOutputStream();
        try{
        	List<OperationLog> logList = queryExecutor.execQuery("com.wuyizhiye.basedata.orepationlog.dao.OperationLogDao.getByCond", getListDataParam(),OperationLog.class);
        	String[] excelHeader = new String[]{"操作用户名称","用户编码","客户机IP","操作描述","操作时间","操作事件","异常信息"}; 
    		String[] excelField = new String[]{"personName","personNumber","clientIp","description","createTime","url","errorMsg"};
    		 
    		if(logList!=null && logList.size()>0){
    			CommonExcelExportUtils.exportExcelByDataList(logList, excelHeader, excelField, os, "异常信息", true,"yyyy-MM-dd HH:mm");
    		}else{
    			os.write("没有可导出的信息".getBytes());
    		}
    		
        }catch(Exception e){
        	os.write(e.getMessage().getBytes());
        }
		os.close();
	}
	 
}
