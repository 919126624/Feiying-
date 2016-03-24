package com.wuyizhiye.base.timing.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.timing.TaskTimeEnum;
import com.wuyizhiye.base.timing.TaskTypeEnum;
import com.wuyizhiye.base.timing.model.Task;
import com.wuyizhiye.base.timing.service.TaskService;
import com.wuyizhiye.base.util.StringUtils;

/**
 * @ClassName TimingTaskUtil
 * @Description 时间任务工具类
 * @author li.biao
 * @date 2015-4-1
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TimingTaskUtil {
	
	/**
	 * 创建定时任务
	 * @param name 任务名称
	 * @param description 任务说明
	 * @param effectDate 生效日期
	 * @param target 任务执行方法 格式:beanName.methodName(paramName,paramName2……)
	 * @param params 任务执行参数，全限字符串类型,格式: value1;value2……
	 * @param type 任务类型:循环执行和单次执行
	 * @param time 执行时间,从零点到六点
	 * @return
	 */
	public static Task createTask(String name,String description,Date effectDate, String target,String params,TaskTypeEnum type,TaskTimeEnum time){
		Task task = new Task();
		task.setId(UUID.randomUUID().toString());
		task.setName(name);
		task.setDescription(description);
		task.setTarget(target);
		task.setParams(params);
		task.setType(type);
		task.setTime(time);
		task.setEffectDate(effectDate);
		task.setLastRunTime(new Date());
		task.setDataCenter(DataSourceHolder.getDataSource());
		TaskService taskService = ApplicationContextAware.getApplicationContext().getBean(TaskService.class);
		taskService.addEntity(task);
		return task;
	}
	
	/**
	 * 执行任务
	 * @param task
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void runTask(Task task) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(task != null){
			if(validateTarget(task.getTarget())){
				task.setTarget(task.getTarget().trim());
				String beanName = getBeanName(task.getTarget());
				Object bean = ApplicationContextAware.getApplicationContext().getBean(beanName);
				if(bean != null){
					Class c = bean.getClass();
					Method method = c.getMethod(getMethodName(task.getTarget()), getMethodParamTypes(task.getTarget()));
					method.invoke(bean, getParams(task.getTarget(),task.getParams()));
				}
			}
		}
	}
	
	private static String getMethodName(String target){
		String methodName = target.substring(target.indexOf(".")+1, target.indexOf("("));
		return methodName;
	}
	
	private static Object[] getParams(String target,String parm){
		String[] paramAttr;
		if(StringUtils.isEmpty(parm)){
			paramAttr = new String[0];
		}else{
			paramAttr =  parm.split(";");
		}
		Class[] types = getMethodParamTypes(target);
		String[] params = new String[types.length];
		for(int i = 0; i < types.length; i++){
			if(i < paramAttr.length){
				params[i] = paramAttr[i];
			}else{
				params[i] = null;
			}
		}
		return params;
	}
	
	private static Class[] getMethodParamTypes(String target){
		String parm = target.substring(target.indexOf("(")+1, target.length() - 1);
		Object[] paramTypes = target.substring(target.indexOf("(")+1, target.length() - 1).split(",");
		if(StringUtils.isEmpty(parm, true)){
			paramTypes = new Object[0];
		}
		Class[] types = new Class[paramTypes.length];
		for(int i = 0; i < types.length; i++){
			types[i] = String.class;
		}
		return types;
	}
	
	private static boolean validateTarget(String target){
		return !StringUtils.isEmpty(target) && target.indexOf(".") > 0 && target.indexOf("(") >0 && target.indexOf(")")==target.length()-1;
	}
	
	private static String getBeanName(String target){
		String beanName = target.split("\\.")[0];
		return beanName;
	}
}
