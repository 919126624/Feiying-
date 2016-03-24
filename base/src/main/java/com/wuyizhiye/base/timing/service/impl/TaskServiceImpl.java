package com.wuyizhiye.base.timing.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.timing.dao.TaskDao;
import com.wuyizhiye.base.timing.model.Task;
import com.wuyizhiye.base.timing.service.TaskService;

/**
 * @ClassName TaskServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-1
 */
@Component(value="timeTaskService")
@Transactional
public class TaskServiceImpl extends BaseServiceImpl<Task> implements TaskService {
	@Autowired
	private TaskDao taskDao;
	@Override
	protected BaseDao getDao() {
 		return taskDao;
	}	
	
	@Override
	public void addEntity(Task entity) {
		entity.setCreateTime(new Date());
		super.addEntity(entity);
	}

	@Override
	public List<Task> getTaskList(Map<String, Object> param) {
		
		return this.taskDao.getTaskList(param);
	}

	@Override
	public void updateSync(Task t) {
		this.taskDao.updateSync(t);
		
	}

	@Override
	public void updateBatchSync(List<Task> tlist) {
		this.taskDao.updateBatchSync(tlist);
		
	}
}