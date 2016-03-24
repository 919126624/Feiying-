package com.wuyizhiye.basedata.person.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.person.dao.SecondPasswordDao;

/**
 * @ClassName SecondPasswordDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="secondPasswordDao")
public class SecondPasswordDaoImpl extends BaseDaoImpl implements SecondPasswordDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.person.dao.SecondPasswordDao";
	}
}
