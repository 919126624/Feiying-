package com.wuyizhiye.base.timing.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.timing.dao.TaskLogDao;
import com.wuyizhiye.base.timing.model.TaskLog;
import com.wuyizhiye.base.timing.service.TaskLogService;

/**
 * @ClassName TaskLogServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-1
 */
@Component(value="taskLogService")
public class TaskLogServiceImpl extends BaseServiceImpl<TaskLog> implements TaskLogService{

	@Autowired
	private TaskLogDao taskLogDao;
	
	@Override
	public List<TaskLog> getTaskLogList(Map<String, Object> param) {
		return taskLogDao.getTaskLogList(param);
	}

	@Override
	protected BaseDao getDao() {
		return taskLogDao;
	}

}
