package com.wuyizhiye.basedata.test.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.test.dao.TestDao;

/**
 * @ClassName TestDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="testDao")
public class TestDaoImpl extends BaseDaoImpl implements TestDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.test.dao.TestDao";
	}

}
