/**
 * com.wuyizhiye.hr.attendance.dao.impl.OverTimeDaoImpl.java
 */
package com.wuyizhiye.hr.attendance.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.attendance.dao.OverTimeDao;

/**
 * @author hyl
 * 
 * @since 2013-11-28
 */
@Component(value="overTimeDao")
public class OverTimeDaoImpl extends BaseDaoImpl implements OverTimeDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.attendance.dao.OverTimeDao";
	}
}
