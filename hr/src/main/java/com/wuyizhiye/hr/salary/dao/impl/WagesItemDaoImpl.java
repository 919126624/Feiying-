/**
 * com.wuyizhiye.hr.salary.dao.impl.WagesItemDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.WagesItemDao;

/**
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="wagesItemDao")
public class WagesItemDaoImpl extends BaseDaoImpl implements WagesItemDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.WagesItemDao";
	}
}
