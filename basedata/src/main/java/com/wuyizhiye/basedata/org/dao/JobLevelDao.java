package com.wuyizhiye.basedata.org.dao;

import java.util.List;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.org.model.JobLevel;

/**
 * @ClassName JobLevelDao
 * @Description 职级Dao
 * @author li.biao
 * @date 2015-4-2
 */
public interface JobLevelDao extends BaseDao {
	void deleteByJob(String job);
	List<JobLevel> getByJob(String job);
}
