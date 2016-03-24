/**
 * com.wuyizhiye.hr.salary.service.impl.StandardItemServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.salary.dao.StandardItemDao;
import com.wuyizhiye.hr.salary.model.StandardItem;
import com.wuyizhiye.hr.salary.service.StandardItemService;

/**
 * 组织service
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="standardItemService")
@Transactional
public class StandardItemServiceImpl extends BaseServiceImpl<StandardItem> implements StandardItemService {
	@Autowired
	private StandardItemDao standardItemDao;
	@Override
	protected BaseDao getDao() {
		return standardItemDao;
	}	
}
