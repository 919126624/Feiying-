package com.wuyizhiye.basedata.person.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.person.dao.DayPersonDao;

/**
 * @ClassName DayPersonDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="dayPersonDao")
public class DayPersonDaoImpl extends BaseDaoImpl implements DayPersonDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.person.dao.DayPersonDao";
	}
}
