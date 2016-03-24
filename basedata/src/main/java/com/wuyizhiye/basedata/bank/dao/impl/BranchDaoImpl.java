package com.wuyizhiye.basedata.bank.dao.impl;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.bank.dao.BranchDao;


/**
 * @ClassName BranchDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="branchDao")
public class BranchDaoImpl extends BaseDaoImpl implements BranchDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.bank.BranchDao";
	}

}
