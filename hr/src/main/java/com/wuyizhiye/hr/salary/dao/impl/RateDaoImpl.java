/**
 * com.wuyizhiye.hr.salary.dao.impl.RateDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.RateDao;

/**
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="rateDao")
public class RateDaoImpl extends BaseDaoImpl implements RateDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.RateDao";
	}
}
