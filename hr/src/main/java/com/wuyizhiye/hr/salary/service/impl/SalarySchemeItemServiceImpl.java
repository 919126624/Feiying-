/**
 * com.wuyizhiye.hr.salary.service.impl.SalarySchemeItemServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.salary.dao.SalarySchemeItemDao;
import com.wuyizhiye.hr.salary.model.SalarySchemeItem;
import com.wuyizhiye.hr.salary.service.SalarySchemeItemService;

/**
 * 薪酬方案项目service
 * @author hlz
 * 
 * @since 2014-02-13
 */
@Component(value="salarySchemeItemService")
@Transactional
public class SalarySchemeItemServiceImpl extends BaseServiceImpl<SalarySchemeItem> implements SalarySchemeItemService {
	@Autowired
	private SalarySchemeItemDao salarySchemeItemDao;
	@Override
	protected BaseDao getDao() {
		return salarySchemeItemDao;
	}	
}
