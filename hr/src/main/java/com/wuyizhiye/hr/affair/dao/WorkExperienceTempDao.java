package com.wuyizhiye.hr.affair.dao;

import java.util.List;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.hr.affair.model.WorkExperience;
/**
 * @author Cai.xing
 * @since 2013-04-08
 */
public interface WorkExperienceTempDao extends BaseDao {
	List<WorkExperience> getByPersonId(String personId);
	
	void deleteByPersonId(String personId);	
}
