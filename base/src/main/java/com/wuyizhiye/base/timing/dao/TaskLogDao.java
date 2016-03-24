package com.wuyizhiye.base.timing.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.timing.model.TaskLog;

/**
 * @ClassName TaskLogDao
 * @Description 定时任务log
 * @author li.biao
 * @date 2015-4-1
 */
public interface TaskLogDao extends BaseDao {

	List<TaskLog> getTaskLogList(Map<String, Object> param);
	
	}
