package com.wuyizhiye.basedata.bank.dao.impl;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.bank.dao.CapaccountDao;


/**
 * @ClassName CapaccountDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="capaccountDao")
public class CapaccountDaoImpl extends BaseDaoImpl implements CapaccountDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.bank.CapaccountDao";
	}

}
