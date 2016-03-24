package com.wuyizhiye.basedata.basic.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.basic.dao.ChangYongYuDao;

/**
 * @ClassName ChangYongYuDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="changYongYuDao")
public class ChangYongYuDaoImpl extends BaseDaoImpl implements ChangYongYuDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.basic.dao.ChangYongYuDao";
	}

}
