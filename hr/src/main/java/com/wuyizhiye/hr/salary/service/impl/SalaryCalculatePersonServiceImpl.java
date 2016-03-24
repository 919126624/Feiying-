/**
 * com.wuyizhiye.hr.salary.service.impl.SalaryCalculatePersonServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.salary.dao.SalaryCalculatePersonDao;
import com.wuyizhiye.hr.salary.model.SalaryCalculatePerson;
import com.wuyizhiye.hr.salary.service.SalaryCalculatePersonService;

/**
 * 核算人员service
 * @author hlz
 * 
 * @since 2014-02-19
 */
@Component(value="salaryCalculatePersonService")
@Transactional
public class SalaryCalculatePersonServiceImpl extends BaseServiceImpl<SalaryCalculatePerson> implements SalaryCalculatePersonService {
	@Autowired
	private SalaryCalculatePersonDao salaryCalculatePersonDao;
	@Override
	protected BaseDao getDao() {
		return salaryCalculatePersonDao;
	}	
}
