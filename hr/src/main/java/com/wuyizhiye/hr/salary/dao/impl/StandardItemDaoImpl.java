/**
 * com.wuyizhiye.hr.salary.dao.impl.StandardItemDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.StandardItemDao;

/**
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="standardItemDao")
public class StandardItemDaoImpl extends BaseDaoImpl implements StandardItemDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.StandardItemDao";
	}
}
