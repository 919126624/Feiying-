/**
 * com.wuyizhiye.hr.salary.service.impl.WagesItemServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.salary.dao.WagesItemDao;
import com.wuyizhiye.hr.salary.model.WagesItem;
import com.wuyizhiye.hr.salary.service.WagesItemService;

/**
 * 组织service
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="wagesItemService")
@Transactional
public class WagesItemServiceImpl extends BaseServiceImpl<WagesItem> implements WagesItemService {
	@Autowired
	private WagesItemDao wagesItemDao;
	@Override
	protected BaseDao getDao() {
		return wagesItemDao;
	}	
}
