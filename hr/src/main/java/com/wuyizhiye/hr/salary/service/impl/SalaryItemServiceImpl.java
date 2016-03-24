/**
 * com.wuyizhiye.hr.salary.service.impl.SalaryItemServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.salary.dao.SalaryItemDao;
import com.wuyizhiye.hr.salary.model.SalaryItem;
import com.wuyizhiye.hr.salary.service.SalaryItemService;

/**
 * 组织service
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="salaryItemService")
@Transactional
public class SalaryItemServiceImpl extends BaseServiceImpl<SalaryItem> implements SalaryItemService {
	@Autowired
	private SalaryItemDao salaryItemDao;
	@Override
	protected BaseDao getDao() {
		return salaryItemDao;
	}	
}
