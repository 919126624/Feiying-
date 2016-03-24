package com.wuyizhiye.basedata.org.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.JobLevelDao;
import com.wuyizhiye.basedata.org.model.JobLevel;

/**
 * @ClassName JobLevelDaoImpl
 * @Description 职级DaoImpl
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="jobLevelDao")
public class JobLevelDaoImpl extends BaseDaoImpl implements JobLevelDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.org.dao.JobLevelDao";
	}

	@Override
	public void deleteByJob(String job) {
		getSqlSession().delete(getNameSpace() + ".deleteByJob", job);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JobLevel> getByJob(String job) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("job", job);
		return getSqlSession().selectList(getNameSpace() + ".select", param);
	}
}
