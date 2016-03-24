package com.wuyizhiye.hr.affair.dao;

import java.util.List;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.hr.affair.model.Education;

/**
 * @author Cai.xing
 * @since 2013-04-08
 */
public interface EducationTempDao extends BaseDao {

	List<Education> getByPersonId(String personId);
	
	void deleteByPersonId(String personId);
}
