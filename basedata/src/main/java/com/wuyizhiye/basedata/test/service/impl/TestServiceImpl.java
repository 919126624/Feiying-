package com.wuyizhiye.basedata.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.test.dao.TestDao;
import com.wuyizhiye.basedata.test.model.Test;
import com.wuyizhiye.basedata.test.service.TestService;

/**
 * @ClassName TestServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="testService")
public class TestServiceImpl extends BaseServiceImpl<Test> implements TestService {

	@Autowired
	protected TestDao testDao;
	
	@Override
	protected BaseDao getDao() {
		return testDao;
	}

}
