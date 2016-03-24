package com.wuyizhiye.basedata.cusresource.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.cusresource.dao.CusResourceTypeDao;

/**
 * @ClassName CusResourceTypeDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="cusResourceTypeDao")
public class CusResourceTypeDaoImpl extends BaseDaoImpl implements CusResourceTypeDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.cusresource.dao.CusResourceTypeDao";
	}

}
