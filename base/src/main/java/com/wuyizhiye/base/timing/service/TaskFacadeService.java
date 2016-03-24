package com.wuyizhiye.base.timing.service;

import java.util.Calendar;

import com.wuyizhiye.base.timing.TaskTimeEnum;
import com.wuyizhiye.base.timing.model.Task;


/**
 * @ClassName TaskFacadeService
 * @Description 任务方法类
 * @author li.biao
 * @date 2015-4-1
 */
public interface TaskFacadeService {
	void runTask(Task task,String personId);
	/**
	 * 定点执行
	 */
	void execute();
	void execute(TaskTimeEnum timeEnum, Calendar cal);
}
