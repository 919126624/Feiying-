package com.wuyizhiye.basedata.basic.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.basic.dao.BasicDataTypeDao;

/**
 * @ClassName BasicDataTypeDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="basicDataTypeDao")
public class BasicDataTypeDaoImpl extends BaseDaoImpl implements
		BasicDataTypeDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.basic.dao.BasicDataTypeDao";
	}

}
