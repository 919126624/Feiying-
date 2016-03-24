package com.wuyizhiye.basedata.org.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.JobLevel;

/**
 * @ClassName JobLevelService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface JobLevelService extends BaseService<JobLevel> {
	void deleteByJob(String job);
}
