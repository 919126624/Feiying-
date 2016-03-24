package com.wuyizhiye.base.timing.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.timing.TaskLastStatusEnum;
import com.wuyizhiye.base.timing.TaskStatusEnum;
import com.wuyizhiye.base.timing.TaskTimeEnum;
import com.wuyizhiye.base.timing.TaskTypeEnum;
import com.wuyizhiye.base.timing.model.Task;
import com.wuyizhiye.base.timing.model.TaskLog;
import com.wuyizhiye.base.timing.service.TaskFacadeService;
import com.wuyizhiye.base.timing.service.TaskLogService;
import com.wuyizhiye.base.timing.service.TaskService;
import com.wuyizhiye.base.timing.util.TimingTaskUtil;

/**
 * @ClassName TaskFacadeServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-1
 */
@Component(value="taskFacadeService")
public class TaskFacadeServiceImpl implements TaskFacadeService {
	private Logger log = Logger.getLogger(TaskFacadeServiceImpl.class);
	@Autowired
	private QueryExecutor queryExecutor;
	@Autowired
	private TaskService taskService;
	@Autowired
	private TaskLogService taskLogService;
	@Override
	public void execute(TaskTimeEnum timeEnum,Calendar cal) {
		int month = cal.get(Calendar.MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);
		int minute = cal.get(Calendar.MINUTE);
		//取了当前时分秒之后再设置时分秒为0用作最后执行时间的条件
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("status", TaskStatusEnum.WAIT);
		param.put("isValid", 1);
//		param.put("time", timeEnum);
		param.put("date", cal.getTime());
		Pagination<Task> tasks = new Pagination<Task>(50, 0);
		tasks = queryExecutor.execQuery("com.wuyizhiye.base.timing.dao.TaskDao.select", tasks, param);
		
		List<Task> taskList;
		do{
			taskList = tasks.getItems();
			for(Task t : taskList){
				
				Date runTime = t.getRunTime();
				if(runTime!=null){
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(runTime);
					int months = calendar.get(Calendar.MONTH);
					int hours = calendar.get(Calendar.HOUR_OF_DAY);
					int days = calendar.get(Calendar.DAY_OF_MONTH);
					int years = calendar.get(Calendar.YEAR);
					int minutes = calendar.get(Calendar.MINUTE);
					if(t.getType().getName().equals("ONCE") && hour == hours && month == months && years == year && minutes == minute && day == days && !t.getLastRunMsg().equals(TaskLastStatusEnum.RUNED.getLabel())){//单次
						runTask(t,null);
					}else if(t.getType().getName().equals("CYCLE") && hour == hours && minutes == minute){//每天循环
						runTask(t,null);
					}else if(t.getType().getName().equals("MONTH") && hour == hours && day == days && minutes == minute){//每月循环
						runTask(t,null);
					}
				}
				
			}
			tasks.setCurrentPage(tasks.getCurrentPage() + 1);
			if(tasks.getCurrentPage() < tasks.getPageCount()){
				tasks = queryExecutor.execQuery("com.wuyizhiye.base.timing.dao.TaskDao.select", tasks, param);
			}
		}while(tasks.getCurrentPage() < tasks.getPageCount());
		
	}
	
	@Transactional
	public void runTask(Task task,String personId){
		TaskLog taskLog = new TaskLog();
		taskLog.setCreateTime(new Date());
		taskLog.setPersonId(personId);
		taskLog.setTask(task);
		task.setLastRunTime(new Date());
		try {
			long start = System.currentTimeMillis();
			TimingTaskUtil.runTask(task);
			task.setTimeConsuming(System.currentTimeMillis() - start);
			task.setTimes(task.getTimes()+1);
			task.setLastRunMsg(TaskLastStatusEnum.RUNED.getLabel());
			taskLog.setInfo(TaskStatusEnum.RUNED.getLabel());
			if(task.getType()==TaskTypeEnum.ONCE){
				task.setStatus(TaskStatusEnum.RUNED);
			}
		} catch (Exception e) {
			task.setStatus(TaskStatusEnum.WAIT);
			task.setLastRunMsg(TaskLastStatusEnum.FAIL.getLabel());
			if("".equals(e.getMessage())){
				taskLog.setInfo("执行失败，无详细");
			}else{
				taskLog.setInfo("执行失败，详细："+e.getMessage());
			}
			log.error(e);
		}finally{
			taskLog.setUUID();
			taskLogService.addEntity(taskLog);
		}
		taskService.updateEntity(task);
	}

	@Override
	public void execute() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		TaskTimeEnum time = null;
		switch(hour){
			case 0:time = TaskTimeEnum.ZERO;break;
			case 1:time = TaskTimeEnum.ONE;break;
			case 2:time = TaskTimeEnum.TWO;break;
			case 3:time = TaskTimeEnum.THREE;break;
			case 4:time = TaskTimeEnum.FOUR;break;
			case 5:time = TaskTimeEnum.FIVE;break;
			case 6:time = TaskTimeEnum.SIX;break;
			default : time = TaskTimeEnum.OTHER;break;
		}
		if(time != null){
			//获取环境下所有的数据源
			List<String> dclist = getDataSourceSingleList();
			for(int i=0;i<dclist.size();i++){
				String dc = dclist.get(i);
				TaskThread taskThred = new TaskThread(time, dc,cal);
				taskThred.start() ;
			}
		}
	}
	
	/**
	 * 得到数据中心列表 返回list
	 */
	private List<String> getDataSourceSingleList(){
		ApplicationContext ctx = ApplicationContextAware.getApplicationContext();
		String[] beanNames = ctx.getBeanNamesForType(BasicDataSource.class);
		List<String> names = new ArrayList<String>();
		for(String n : beanNames){
			names.add(n);
		}
		return names;
	}
	
	/**
	 * 线程实现各个数据源同步
	 * @author li.biao
	 * @since 2013-11-5
	 */
	class TaskThread extends Thread { 
		private String dataCenter = "" ;
		private TaskTimeEnum time  ;
		private Calendar cal;
		public TaskThread(TaskTimeEnum t,String dc,Calendar cal) {
			this.time = t ;
			this.dataCenter = dc ;
			this.cal = cal;
	    }  
	    public void run() { 
	    	if(!StringUtils.isEmpty(dataCenter)){
	    		DataSourceHolder.setDataSource(dataCenter);	
				execute(time,cal);
	    	}
	    }
	}
	
}
