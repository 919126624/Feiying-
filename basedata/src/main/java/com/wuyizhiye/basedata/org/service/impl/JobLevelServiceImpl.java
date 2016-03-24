package com.wuyizhiye.basedata.org.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.org.dao.JobLevelDao;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.service.JobLevelService;

/**
 * @ClassName JobLevelServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="jobLevelService")
@Transactional
public class JobLevelServiceImpl extends BaseServiceImpl<JobLevel> implements
		JobLevelService {
	@Autowired
	private JobLevelDao jobLevelDao;
	@Override
	protected BaseDao getDao() {
		return jobLevelDao;
	}
	@Override
	public void deleteByJob(String job) {
		jobLevelDao.deleteByJob(job);
	}

}
