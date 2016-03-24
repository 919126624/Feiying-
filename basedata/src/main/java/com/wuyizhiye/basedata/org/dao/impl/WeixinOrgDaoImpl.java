package com.wuyizhiye.basedata.org.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.WeixinOrgDao;

/**
 * @ClassName WeixinOrgDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="weixinOrgDao")
public class WeixinOrgDaoImpl extends BaseDaoImpl implements WeixinOrgDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.org.dao.WeixinOrgDao";
	}
}
