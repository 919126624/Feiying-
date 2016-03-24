package com.wuyizhiye.hr.dao;

import java.util.List;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.hr.model.Education;

/**
 *教育经历DAo
 * @author taking.wang
 * @since 2013-01-18
 *
 */
public interface EducationDao extends BaseDao {

	List<Education> getByPersonId(String personId);
	
	void deleteByPersonId(String personId);
}
