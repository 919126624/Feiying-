package com.wuyizhiye.hr.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.model.WorkExperience;

/**
 * 工作经历service
 * @author taking.wang
 * @since 2013-01-18
 */
public interface WorkExperienceService extends BaseService<WorkExperience> {

	List<WorkExperience> getByPersonId(String personId);
	void deleteByPersonId(String personId);
}
