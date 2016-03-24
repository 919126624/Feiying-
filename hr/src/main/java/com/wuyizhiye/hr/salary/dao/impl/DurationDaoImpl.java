/**
 * com.wuyizhiye.hr.salary.dao.impl.DurationDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.DurationDao;

/**
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="durationDao")
public class DurationDaoImpl extends BaseDaoImpl implements DurationDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.DurationDao";
	}
}
