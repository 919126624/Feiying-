package com.wuyizhiye.hr.affair.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.affair.model.Education;

/**
 *教育经历Service
 * @author taking.wang
 * @since 2013-01-18
 *
 */
public interface EducationTempService extends BaseService<Education>{

	public List<Education> getByPersonId(String personId);
}
