/**
 * com.wuyizhiye.hr.affair.dao.impl.PositiveDaoImpl.java
 */
package com.wuyizhiye.hr.affair.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.affair.dao.PositiveDao;

/**
 * @author Cai.xing
 * @since 2013-04-08
 */
@Component(value="positiveDao")
public class PositiveDaoImpl extends BaseDaoImpl implements PositiveDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.affair.dao.PositiveDao";
	}
}
