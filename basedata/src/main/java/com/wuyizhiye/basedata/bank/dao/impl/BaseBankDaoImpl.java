package com.wuyizhiye.basedata.bank.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.bank.dao.BaseBankDao;

/**
 * @ClassName BaseBankDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="baseBankDao")
public class BaseBankDaoImpl extends BaseDaoImpl implements BaseBankDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.bank.dao.BaseBankDao";
	}
	
	
}
