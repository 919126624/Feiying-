package com.wuyizhiye.basedata.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.impl.QueryExecutorImpl;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.operationlog.model.OperationLog;
import com.wuyizhiye.basedata.orepationlog.service.OperationLogService;
import com.wuyizhiye.basedata.param.model.ParamLines;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName OperationLogUtil
 * @Description 操作日志 工具类
 * @author li.biao
 * @date 2015-4-2
 */
public class OperationLogUtil {
	
	 /**
	  * 记录操作日志
	  * @param url URL或 执行方法的类路径如：hr/person/list 或 com.wuyizhiye.hr.controller.HrPersonListController.list
	  * @param description 操作描述
	  * @param errorMsg  异常信息
	  * @param executeStatus 执行状态：无""、成功 、失败
	  */
	public static void saveOperationLog(String url,String description,String errorMsg,String executeStatus){
		OperationLog log = new OperationLog();
		log.setUUID();
		log.setSuffix(getSuffix(new Date()));
		log.setCreateTime(new Date());
		Person person = SystemUtil.getCurrentUser();
		if(person==null){
			return ;
		}
		log.setPersonId(person.getId());
		log.setPersonName(person.getName());
		log.setPersonNumber(person.getNumber());
		log.setClientIp(person.getCurrentLoginIp());
		log.setDescription(description);
		log.setUrl(url);
		log.setErrorMsg(errorMsg);
		log.setExecuteStatus(executeStatus); 
		OperationLogService taskService = ApplicationContextAware.getApplicationContext().getBean(OperationLogService.class);
		try {
			taskService.addEntity(log);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("tableName", "T_BD_OPERATIONLOG"+OperationLogUtil.getSuffix(new Date()));
			map.put("paramnumber", "DataBaseType");
			ParamLines paramLines = QueryExecutorImpl.getInstance().execOneEntity("com.wuyizhiye.basedata.param.dao.ParamLinesDao.selectByTypeNumber", map, ParamLines.class);
			String dataBaseType = paramLines.getParamValue();
			if(dataBaseType.equals("ORACLE")){
				QueryExecutorImpl.getInstance().executeUpdate("com.wuyizhiye.basedata.orepationlog.dao.OperationLogDao.createTable", map);
			}else{
				QueryExecutorImpl.getInstance().executeUpdate("com.wuyizhiye.basedata.orepationlog.dao.OperationLogDao.createTableOfMysql", map);
			}
			saveOperationLog(url,description,errorMsg,executeStatus);
		}
	}
	
	public static void saveWxOperationLog(String url,String ip,String personNum,String description){
		OperationLog log = new OperationLog();
		log.setSuffix(getSuffix(new Date()));
		log.setUUID();
		log.setCreateTime(new Date());
		log.setDescription(description);
		if(StringUtils.isEmpty(personNum)) return ;
		Person person = null;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("number", personNum);
		List<Person> ulist = 
			QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.selectPersonList", map, Person.class);
		if(ulist.size()>0){
			person = ulist.get(0);
		}else{
			return;
		}
		log.setClientIp(ip);
		log.setPersonId(person.getId());
		log.setPersonName(person.getName());
		log.setPersonNumber(person.getNumber());
		log.setDescription(description);
		log.setUrl(url);
		OperationLogService oprService = ApplicationContextAware.getApplicationContext().getBean(OperationLogService.class);
		try {
			oprService.addEntity(log);
		} catch (Exception e) {
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("tableName", "T_BD_OPERATIONLOG"+OperationLogUtil.getSuffix(new Date()));
			param.put("paramnumber", "DataBaseType");
			ParamLines paramLines = QueryExecutorImpl.getInstance().execOneEntity("com.wuyizhiye.basedata.param.dao.ParamLinesDao.selectByTypeNumber", param, ParamLines.class);
			String dataBaseType = paramLines.getParamValue();
			if(dataBaseType.equals("ORACLE")){
				QueryExecutorImpl.getInstance().executeUpdate("com.wuyizhiye.basedata.orepationlog.dao.OperationLogDao.createTable", param);
			}else{
				QueryExecutorImpl.getInstance().executeUpdate("com.wuyizhiye.basedata.orepationlog.dao.OperationLogDao.createTableOfMysql", param);
			}
			saveWxOperationLog(url,ip,personNum,description);
		}
	}
	
	/**
	 * 得到当月表名
	 * @return
	 */
	public static String getSuffix(Date date){
		String suffix = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);	
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		suffix += "_"+year+"_"+month;
		return suffix;
	}
	
}
