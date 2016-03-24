package com.wuyizhiye.base.timing.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.base.timing.dao.TaskLogDao;
import com.wuyizhiye.base.timing.model.TaskLog;

/**
 * @ClassName TaskLogDaoImpl
 * @Description 定时任务log
 * @author li.biao
 * @date 2015-4-1
 */
@Component(value="taskLogDao")
public class TaskLogDaoImpl extends BaseDaoImpl implements TaskLogDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.base.timing.dao.TaskLogDao";
	}

	@Override
	public List<TaskLog> getTaskLogList(Map<String, Object> param) {
		return this.getSqlSession().selectList(getNameSpace()+".select", param);
	}

}
