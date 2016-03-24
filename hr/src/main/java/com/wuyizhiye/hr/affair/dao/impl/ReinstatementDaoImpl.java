/**
 * com.wuyizhiye.hr.affair.dao.impl.ReinstatementDaoImpl.java
 */
package com.wuyizhiye.hr.affair.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.affair.dao.ReinstatementDao;

/**
 * @author Cai.xing
 * @since 2013-04-08
 */
@Component(value="reinstatementDao")
public class ReinstatementDaoImpl extends BaseDaoImpl implements ReinstatementDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.affair.dao.ReinstatementDao";
	}
}
