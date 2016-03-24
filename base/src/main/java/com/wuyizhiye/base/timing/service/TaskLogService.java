package com.wuyizhiye.base.timing.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.timing.model.TaskLog;

/**
 * @ClassName TaskLogService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-1
 */
public interface TaskLogService extends BaseService<TaskLog> {
	List<TaskLog> getTaskLogList(Map<String,Object> param);
}
