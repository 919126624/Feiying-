/**
 * com.wuyizhiye.hr.salary.service.impl.SalarySchemeObjectServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.salary.dao.SalarySchemeObjectDao;
import com.wuyizhiye.hr.salary.model.SalarySchemeObject;
import com.wuyizhiye.hr.salary.service.SalarySchemeObjectService;

/**
 * 薪酬方案实施对象service
 * @author hlz
 * 
 * @since 2014-02-13
 */
@Component(value="salarySchemeObjectService")
@Transactional
public class SalarySchemeObjectServiceImpl extends BaseServiceImpl<SalarySchemeObject> implements SalarySchemeObjectService {
	@Autowired
	private SalarySchemeObjectDao salarySchemeObjectDao;
	@Override
	protected BaseDao getDao() {
		return salarySchemeObjectDao;
	}	
}
