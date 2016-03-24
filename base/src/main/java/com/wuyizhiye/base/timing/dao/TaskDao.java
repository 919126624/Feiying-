package com.wuyizhiye.base.timing.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.timing.model.Task;

/**
 * @ClassName TaskDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-1
 */
public interface TaskDao extends BaseDao {
	List<Task> getTaskList(Map<String,Object> param);
	void updateSync(Task t);
	void updateBatchSync(List<Task> tlist);
}
