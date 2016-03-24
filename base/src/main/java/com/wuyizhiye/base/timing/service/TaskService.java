package com.wuyizhiye.base.timing.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.timing.model.Task;

/**
 * @ClassName TaskService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-1
 */
public interface TaskService extends BaseService<Task> {
	List<Task> getTaskList(Map<String,Object> param);
	void updateSync(Task t);
	void updateBatchSync(List<Task> tlist);
}
