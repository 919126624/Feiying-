package com.wuyizhiye.hr.dao;

import java.util.List;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.hr.model.WorkExperience;

/**
 * 工作经历dao
 * @author taking.wang
 * @since 2013-01-18
 */
public interface WorkExperienceDao extends BaseDao {
	List<WorkExperience> getByPersonId(String personId);
	
	void deleteByPersonId(String personId);	
}
