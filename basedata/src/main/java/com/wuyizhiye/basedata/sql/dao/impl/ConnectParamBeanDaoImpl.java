package com.wuyizhiye.basedata.sql.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.sql.dao.ConnectParamBeanDao;

/**
 * @ClassName ConnectParamBeanDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="connectParamBeanDao")
public class ConnectParamBeanDaoImpl extends BaseDaoImpl implements ConnectParamBeanDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.sql.ConnectParamBeanDao";
	}
}
