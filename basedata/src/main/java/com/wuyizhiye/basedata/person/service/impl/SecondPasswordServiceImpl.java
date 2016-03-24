package com.wuyizhiye.basedata.person.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.person.dao.SecondPasswordDao;
import com.wuyizhiye.basedata.person.model.SecondPassword;
import com.wuyizhiye.basedata.person.service.SecondPasswordService;

/**
 * @ClassName SecondPasswordServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="secondPasswordService")
@Transactional
public class SecondPasswordServiceImpl extends BaseServiceImpl<SecondPassword> implements SecondPasswordService {
	@Autowired
	private SecondPasswordDao secondPasswordDao;
	@Override
	protected BaseDao getDao() {
		return secondPasswordDao;
	}	
}