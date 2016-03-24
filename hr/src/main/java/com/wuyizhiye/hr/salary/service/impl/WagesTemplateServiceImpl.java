/**
 * com.wuyizhiye.hr.salary.service.impl.WagesTemplateServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.salary.dao.WagesTemplateDao;
import com.wuyizhiye.hr.salary.model.WagesTemplate;
import com.wuyizhiye.hr.salary.service.WagesTemplateService;

/**
 * 组织service
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="wagesTemplateService")
@Transactional
public class WagesTemplateServiceImpl extends BaseServiceImpl<WagesTemplate> implements WagesTemplateService {
	@Autowired
	private WagesTemplateDao wagesTemplateDao;
	@Override
	protected BaseDao getDao() {
		return wagesTemplateDao;
	}	
}
