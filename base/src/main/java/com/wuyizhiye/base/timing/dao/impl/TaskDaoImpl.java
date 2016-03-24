package com.wuyizhiye.base.timing.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.base.timing.dao.TaskDao;
import com.wuyizhiye.base.timing.model.Task;

/**
 * @ClassName TaskDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-1
 */
@Component(value="taskDao")
public class TaskDaoImpl extends BaseDaoImpl implements TaskDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.base.timing.dao.TaskDao";
	}

	@Override
	public List<Task> getTaskList(Map<String, Object> param) {
		
		return this.getSqlSession().selectList(getNameSpace()+".select", param);
	}

	@Override
	public void updateSync(Task t) {
		this.getSqlSession().update(getNameSpace()+".updateSync", t);
		
	}

	@Override
	public void updateBatchSync(List<Task> tlist) {
		for(Task t : tlist){
			this.updateSync(t);
		}
		
	}



}
