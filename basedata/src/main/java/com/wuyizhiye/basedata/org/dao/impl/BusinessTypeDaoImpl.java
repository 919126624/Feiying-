package com.wuyizhiye.basedata.org.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.BusinessTypeDao;

/**
 * @ClassName BusinessTypeDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="businessTypeDao")
public class BusinessTypeDaoImpl extends BaseDaoImpl implements BusinessTypeDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.org.dao.BusinessTypeDao";
	}

}
