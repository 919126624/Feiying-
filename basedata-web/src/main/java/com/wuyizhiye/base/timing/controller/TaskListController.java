package com.wuyizhiye.base.timing.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.timing.TaskStatusEnum;
import com.wuyizhiye.base.timing.TaskTimeEnum;
import com.wuyizhiye.base.timing.TaskTypeEnum;
import com.wuyizhiye.base.timing.model.Task;
import com.wuyizhiye.base.timing.service.TaskFacadeService;
import com.wuyizhiye.base.timing.service.TaskService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName TaskListController
 * @Description 定时任务列表
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="/base/task/*")
public class TaskListController extends ListController {
	@Autowired
	private TaskService taskService;
	@Autowired
	private TaskFacadeService taskFacadeService;
	@Override
	protected CoreEntity createNewEntity() {
		Task task = new Task();
		return task;
	}

	@Override
	protected String getListView() {
		return "base/timing/taskList";
	}

	@Override
	protected String getEditView() {
		getRequest().setAttribute("types", TaskTypeEnum.values());
		getRequest().setAttribute("times", TaskTimeEnum.values());
		if(getString("isValid")!=null){
			getRequest().setAttribute("isValid", getString("isValid"));
		}
		getRequest().setAttribute("cuurDate", new Date());
		return "base/timing/taskEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.base.timing.dao.TaskDao.select";
	}

	@Override
	protected TaskService getService() {
		return taskService;
	}
	
	@RequestMapping(value="runTask")
	public void runTask(@RequestParam(value="id")String taskId,HttpServletResponse response) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		Task task = taskService.getEntityById(taskId);
		getOutputMsg().put("STATE", "SUCCESS");
		if(task!=null){
			if(task.getStatus()==TaskStatusEnum.WAIT){
				taskFacadeService.runTask(task,getCurrentUser().getId());
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("msg", "只能执行等待中的任务");
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@Override
	protected JsonConfig getDefaultJsonConfig() {
		JsonConfig config = super.getDefaultJsonConfig();
		config.registerJsonValueProcessor(TaskTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof TaskTypeEnum){
					TaskTypeEnum v = (TaskTypeEnum)value;
					JSONObject jo = new JSONObject();
					jo.put("name", v.getName());
					jo.put("label", v.getLabel());
					return jo;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				return null;
			}
		});
		config.registerJsonValueProcessor(TaskStatusEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof TaskStatusEnum){
					TaskStatusEnum v = (TaskStatusEnum)value;
					JSONObject jo = new JSONObject();
					jo.put("name", v.getName());
					jo.put("label", v.getLabel());
					return jo;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				return null;
			}
		});
		config.registerJsonValueProcessor(TaskTimeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof TaskTimeEnum){
					TaskTimeEnum v = (TaskTimeEnum)value;
					JSONObject jo = new JSONObject();
					jo.put("name", v.getName());
					jo.put("label", v.getLabel());
					return jo;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				return null;
			}
		});
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
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
	 * 日志数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="queryTaskLog")
	public void queryTaskLog(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery("com.wuyizhiye.base.timing.dao.TaskLogDao.select", pagination, getListDataParam());
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 日志页面
	 * @param response
	 * @return
	 */
	@RequestMapping(value="taskLogPage")
	public String taskLogPage(HttpServletResponse response,ModelMap map){
		String taskId = getString("taskId");
		String taskName = getString("taskName");
		map.put("taskId", taskId);
		map.put("taskName", taskName);
		return "base/timing/taskLogList";
	}
	
	/**
	 * 设置状态
	 * @param response
	 */
	@RequestMapping(value="setStatus")
	public void setStatus(HttpServletResponse response){
		Task task = taskService.getEntityById(getString("id"));
		if(task.getIsValid() == 1){
			task.setIsValid(0);
		}else{
			task.setIsValid(1);
		}
		queryExecutor.executeUpdate("com.wuyizhiye.base.timing.dao.TaskDao.updateSync", task);
		outPrint(response, getOutputMsg());
	}
	
	
}
