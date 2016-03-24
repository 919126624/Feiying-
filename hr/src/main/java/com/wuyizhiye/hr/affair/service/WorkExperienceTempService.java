package com.wuyizhiye.hr.affair.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.affair.model.WorkExperience;
/**
 * @author Cai.xing
 * @since 2013-04-08
 */
public interface WorkExperienceTempService extends BaseService<WorkExperience> {

	List<WorkExperience> getByPersonId(String personId);
}
