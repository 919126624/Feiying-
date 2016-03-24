package com.wuyizhiye.basedata.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.dao.LogoConfigDao;

/**
 * @ClassName LogoConfigDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="logoConfigDao")
public class LogoConfigDaoImpl extends BaseDaoImpl implements LogoConfigDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.dao.LogoConfigDao";
	}
}
