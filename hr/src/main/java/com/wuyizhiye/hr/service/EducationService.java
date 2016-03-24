package com.wuyizhiye.hr.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.model.Education;

/**
 *教育经历Service
 * @author taking.wang
 * @since 2013-01-18
 *
 */
public interface EducationService extends BaseService<Education>{

	public List<Education> getByPersonId(String personId);
	void deleteByPersonId(String personId);
}
