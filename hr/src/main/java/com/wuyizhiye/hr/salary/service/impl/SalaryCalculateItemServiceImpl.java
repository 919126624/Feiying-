/**
 * com.wuyizhiye.hr.salary.service.impl.SalaryCalculateItemServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.salary.dao.SalaryCalculateItemDao;
import com.wuyizhiye.hr.salary.model.SalaryCalculateItem;
import com.wuyizhiye.hr.salary.service.SalaryCalculateItemService;

/**
 * 核算项目service
 * @author hlz
 * 
 * @since 2014-02-19
 */
@Component(value="salaryCalculateItemService")
@Transactional
public class SalaryCalculateItemServiceImpl extends BaseServiceImpl<SalaryCalculateItem> implements SalaryCalculateItemService {
	@Autowired
	private SalaryCalculateItemDao salaryCalculateItemDao;
	@Override
	protected BaseDao getDao() {
		return salaryCalculateItemDao;
	}	
}
